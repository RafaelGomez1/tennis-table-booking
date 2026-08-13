package com.codely.members.application.delete

import arrow.core.raise.Raise
import com.codely.members.domain.MemberFindByCriteria
import com.codely.members.domain.MemberId
import com.codely.members.domain.MemberRepository

context(MemberRepository, Raise<DeleteMemberError>)
suspend fun deleteMember(id: MemberId) {
    find(MemberFindByCriteria.ById(id)) ?: raise(DeleteMemberError.MemberNotFound)

    delete(id)
}
