package com.codely.member.application.delete

import arrow.core.raise.Raise
import arrow.core.raise.catch
import com.codely.member.domain.MemberId
import com.codely.member.domain.MemberRepository
import java.util.UUID

context(MemberRepository, Raise<DeleteMemberError>)
suspend fun handle(command: DeleteMemberCommand) {
    val id = catch({ MemberId(UUID.fromString(command.id)) }) { raise(DeleteMemberError.InvalidUUID) }

    deleteMember(id)
}

data class DeleteMemberCommand(val id: String)

sealed class DeleteMemberError {
    data object InvalidUUID : DeleteMemberError()
    data object MemberNotFound : DeleteMemberError()
}
