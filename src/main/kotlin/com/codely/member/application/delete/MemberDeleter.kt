package com.codely.member.application.delete

import arrow.core.raise.Raise
import com.codely.member.domain.MemberFindByCriteria
import com.codely.member.domain.MemberId
import com.codely.member.domain.MemberRepository

context(MemberRepository, Raise<DeleteMemberError>)
suspend fun deleteMember(id: MemberId) {
    find(MemberFindByCriteria.ById(id)) ?: raise(DeleteMemberError.MemberNotFound)

    delete(id)
}
