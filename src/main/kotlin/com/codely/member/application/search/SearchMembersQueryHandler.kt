package com.codely.member.application.search

import arrow.core.raise.Raise
import com.codely.member.domain.Member
import com.codely.member.domain.MemberRepository
import com.codely.member.domain.MemberSearchByCriteria
import com.codely.member.domain.MemberSearchByCriteria.All
import com.codely.member.domain.MemberType
import com.codely.member.domain.Page
import com.codely.member.domain.PageRequest

context(MemberRepository, Raise<SearchMembersError>)
suspend fun handle(query: SearchMembersQuery): Page<Member> {
    val memberType = query.type?.let { MemberType.fromString(it) ?: raise(SearchMembersError.InvalidType) }
    val criteria = memberType?.let { MemberSearchByCriteria.ByType(it) } ?: All
    val pageRequest = PageRequest(page = query.page, size = query.size)

    return searchMembers(criteria, pageRequest)
}

data class SearchMembersQuery(
    val type: String? = null,
    val page: Int = 0,
    val size: Int = 20
)

sealed class SearchMembersError {
    data object InvalidType : SearchMembersError()
}
