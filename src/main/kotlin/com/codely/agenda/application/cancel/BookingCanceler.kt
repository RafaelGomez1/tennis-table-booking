package com.codely.agenda.application.cancel

import arrow.core.raise.Raise
import arrow.core.raise.recover
import com.codely.agenda.application.cancel.CancelBookingError.AgendaNotFound
import com.codely.agenda.application.cancel.CancelBookingError.DomainError
import com.codely.agenda.domain.*
import com.codely.agenda.domain.AgendaFindByCriteria.ById
import java.util.*

context(AgendaRepository, Raise<CancelBookingError>)
suspend fun cancelBooking(id: UUID, name: Player, hourId: UUID): Agenda {
    val agenda = findOrElse(ById(id)) { AgendaNotFound }

    val updatedAgenda = recover({ agenda.cancelBooking(hourId, name) }) { raise(DomainError(it)) }

    save(updatedAgenda)
    return updatedAgenda
}

sealed class CancelBookingError {
    data object InvalidUUID : CancelBookingError()
    data object InvalidPlayerName : CancelBookingError()
    data object AgendaNotFound : CancelBookingError()
    class DomainError(val error: CancelBookingErrorDomain): CancelBookingError()
}
