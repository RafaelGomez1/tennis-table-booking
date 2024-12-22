package com.codely.agenda.application.book

import arrow.core.raise.Raise
import arrow.core.raise.recover
import com.codely.agenda.application.book.BookAgendaError.AgendaNotFound
import com.codely.agenda.application.book.BookAgendaError.DomainError
import com.codely.agenda.domain.Agenda
import com.codely.agenda.domain.AgendaFindByCriteria.ById
import com.codely.agenda.domain.AgendaRepository
import com.codely.agenda.domain.BookAgendaErrorDomain
import com.codely.agenda.domain.Player
import com.codely.agenda.domain.findOrElse
import java.util.UUID

context(AgendaRepository, Raise<BookAgendaError>)
suspend fun bookAgenda(id: UUID, name: Player, hourId: UUID): Agenda {
    val agenda = findOrElse(ById(id)) { AgendaNotFound }

    val updatedAgenda =
        recover({ agenda.bookAvailableHour(hourId, name) }) { raise(DomainError(it)) }

    save(updatedAgenda)
    return updatedAgenda
}

sealed class BookAgendaError {
    data object InvalidUUID : BookAgendaError()
    data object InvalidPlayerName : BookAgendaError()
    data object AgendaNotFound : BookAgendaError()
    class DomainError(val error: BookAgendaErrorDomain) : BookAgendaError()
}
