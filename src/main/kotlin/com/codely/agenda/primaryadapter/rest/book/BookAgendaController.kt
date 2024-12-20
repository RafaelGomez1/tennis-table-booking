package com.codely.agenda.primaryadapter.rest.book

import arrow.core.raise.fold
import com.codely.agenda.application.book.BookAgendaCommand
import com.codely.agenda.application.book.BookAgendaError
import com.codely.agenda.application.book.BookAgendaError.*
import com.codely.agenda.application.book.handle
import com.codely.agenda.domain.AgendaRepository
import com.codely.agenda.domain.BookAgendaErrorDomain
import com.codely.agenda.domain.BookAgendaErrorDomain.*
import com.codely.agenda.primaryadapter.rest.error.AgendaServerErrors.AGENDA_DOES_NOT_EXIST
import com.codely.agenda.primaryadapter.rest.error.AgendaServerErrors.AVAILABLE_HOUR_DOES_NOT_EXIST
import com.codely.agenda.primaryadapter.rest.error.AgendaServerErrors.INVALID_IDENTIFIERS
import com.codely.agenda.primaryadapter.rest.error.AgendaServerErrors.INVALID_PLAYER_NAME
import com.codely.agenda.primaryadapter.rest.error.AgendaServerErrors.MAX_CAPACITY_REACHED
import com.codely.agenda.primaryadapter.rest.error.AgendaServerErrors.USER_ALREADY_BOOKED
import com.codely.shared.cors.BaseController
import com.codely.shared.response.Response
import com.codely.shared.response.withBody
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpStatus.*
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class BookAgendaController(private val repository: AgendaRepository) : BaseController() {

    @PostMapping("/api/agendas/{agendaId}/hours/{hourId}")
    fun bookAgenda(@PathVariable agendaId: String, @PathVariable hourId: String, @RequestBody body: BookAgendaDTO): Response<*> = runBlocking {
        with(repository) {
            fold(
                block = { handle(BookAgendaCommand(id = agendaId, hourId = hourId, playerName = body.playerName)) },
                recover = { error -> error.toServerError() },
                transform = { agenda -> Response.status(OK).body(agenda) }
            )
        }
    }

    private fun BookAgendaError.toServerError() =
        when (this) {
            is AgendaNotFound -> Response.status(NOT_FOUND).withBody(AGENDA_DOES_NOT_EXIST)
            is InvalidUUID -> Response.status(BAD_REQUEST).withBody(INVALID_IDENTIFIERS)
            is InvalidPlayerName -> Response.status(BAD_REQUEST).withBody(INVALID_PLAYER_NAME)
            is DomainError -> error.toServerError()
        }

    private fun BookAgendaErrorDomain.toServerError() =
        when(this) {
            is AvailableHourNotFound -> Response.status(NOT_FOUND).withBody(AVAILABLE_HOUR_DOES_NOT_EXIST)
            is MaxCapacityReached -> Response.status(CONFLICT).withBody(MAX_CAPACITY_REACHED)
            is PlayerAlreadyBooked -> Response.status(CONFLICT).withBody(USER_ALREADY_BOOKED)
        }
}
