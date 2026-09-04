package com.codely.members.application.delete

import arrow.core.raise.Raise
import arrow.core.raise.catch
import com.codely.departures.domain.Departure
import com.codely.departures.domain.DepartureDate
import com.codely.departures.domain.DepartureRepository
import com.codely.members.domain.MemberId
import com.codely.members.domain.MemberFindByCriteria
import com.codely.members.domain.MemberRepository
import java.time.LocalDate
import java.util.UUID

context(MemberRepository, DepartureRepository, Raise<DeleteMemberError>)
suspend fun handle(command: DeleteMemberCommand) {
    val id = catch({ MemberId(UUID.fromString(command.id)) }) { raise(DeleteMemberError.InvalidUUID) }

    val member = find(MemberFindByCriteria.ById(id)) ?: raise(DeleteMemberError.MemberNotFound)

    catch({
        val departureDate = DepartureDate(LocalDate.parse(command.departureDate))
        val departure = Departure.fromMember(member, departureDate)
        save(departure)
    }) {
        raise(DeleteMemberError.FailedToCreateDeparture)
    }

    delete(id)
}

data class DeleteMemberCommand(val id: String, val departureDate: String)

sealed class DeleteMemberError {
    data object InvalidUUID : DeleteMemberError()
    data object MemberNotFound : DeleteMemberError()
    data object FailedToCreateDeparture : DeleteMemberError()
}
