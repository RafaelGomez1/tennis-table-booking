package com.codely.members.application.delete

import arrow.core.raise.Raise
import arrow.core.raise.catch
import arrow.core.raise.recover
import com.codely.departures.application.create.CreateDepartureError
import com.codely.departures.application.create.createDeparture
import com.codely.departures.domain.DepartureRepository
import com.codely.members.domain.MemberId
import com.codely.members.domain.MemberFindByCriteria
import com.codely.members.domain.MemberRepository
import java.util.UUID

context(MemberRepository, DepartureRepository, Raise<DeleteMemberError>)
suspend fun handle(command: DeleteMemberCommand) {
    val id = catch({ MemberId(UUID.fromString(command.id)) }) { raise(DeleteMemberError.InvalidUUID) }

    val member = find(MemberFindByCriteria.ById(id)) ?: raise(DeleteMemberError.MemberNotFound)

    command.departureDate?.let { departureDate ->
        catch({
            recover({
                createDeparture(member, departureDate)
                Unit
            }) { _: CreateDepartureError ->
                raise(DeleteMemberError.FailedToCreateDeparture)
            }
        }) {
            raise(DeleteMemberError.FailedToCreateDeparture)
        }
    }

    delete(id)
}

data class DeleteMemberCommand(val id: String, val departureDate: String? = null)

sealed class DeleteMemberError {
    data object InvalidUUID : DeleteMemberError()
    data object MemberNotFound : DeleteMemberError()
    data object FailedToCreateDeparture : DeleteMemberError()
}
