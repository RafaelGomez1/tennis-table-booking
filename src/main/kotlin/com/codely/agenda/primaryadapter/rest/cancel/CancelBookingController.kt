package com.codely.agenda.primaryadapter.rest.cancel

import arrow.core.raise.fold
import com.codely.agenda.application.cancel.CancelBookingCommand
import com.codely.agenda.application.cancel.CancelBookingError
import com.codely.agenda.application.cancel.CancelBookingError.AgendaNotFound
import com.codely.agenda.application.cancel.CancelBookingError.DomainError
import com.codely.agenda.application.cancel.CancelBookingError.InvalidPlayerName
import com.codely.agenda.application.cancel.CancelBookingError.InvalidUUID
import com.codely.agenda.application.cancel.handle
import com.codely.agenda.domain.AgendaRepository
import com.codely.agenda.domain.CancelBookingErrorDomain
import com.codely.agenda.domain.CancelBookingErrorDomain.AvailableHourNotFound
import com.codely.agenda.domain.CancelBookingErrorDomain.PlayerNotBooked
import com.codely.agenda.primaryadapter.rest.error.AgendaServerErrors.AGENDA_DOES_NOT_EXIST
import com.codely.agenda.primaryadapter.rest.error.AgendaServerErrors.AVAILABLE_HOUR_DOES_NOT_EXIST
import com.codely.agenda.primaryadapter.rest.error.AgendaServerErrors.INVALID_IDENTIFIERS
import com.codely.agenda.primaryadapter.rest.error.AgendaServerErrors.INVALID_PLAYER_NAME
import com.codely.agenda.primaryadapter.rest.error.AgendaServerErrors.USER_NOT_BOOKED
import com.codely.shared.cors.BaseController
import com.codely.shared.response.Response
import com.codely.shared.response.withBody
import kotlinx.coroutines.coroutineScope
import org.springframework.http.HttpStatus.OK
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CancelBookingController(private val repository: AgendaRepository) : BaseController() {

    @PatchMapping("/api/agendas/{agendaId}/hours/{hourId}")
    suspend fun cancel(
        @PathVariable agendaId: String,
        @PathVariable hourId: String,
        @RequestBody body: CancelBookingDTO
    ): Response<*> = coroutineScope {
        with(repository) {
            fold(
                block = { handle(CancelBookingCommand(id = agendaId, hourId = hourId, playerName = body.playerName)) },
                recover = { error -> error.toServerError() },
                transform = { agenda -> Response.status(OK).body(agenda) }
            )
        }
    }

    private fun CancelBookingError.toServerError(): Response<*> =
        when (this) {
            is AgendaNotFound -> Response.status(NOT_FOUND).withBody(AGENDA_DOES_NOT_EXIST)
            is InvalidUUID -> Response.status(BAD_REQUEST).withBody(INVALID_IDENTIFIERS)
            is InvalidPlayerName -> Response.status(BAD_REQUEST).withBody(INVALID_PLAYER_NAME)
            is DomainError -> error.toServerError()
        }

    private fun CancelBookingErrorDomain.toServerError() =
        when (this) {
            is AvailableHourNotFound -> Response.status(NOT_FOUND).withBody(AVAILABLE_HOUR_DOES_NOT_EXIST)
            is PlayerNotBooked -> Response.status(NOT_FOUND).withBody(USER_NOT_BOOKED)
        }
}
