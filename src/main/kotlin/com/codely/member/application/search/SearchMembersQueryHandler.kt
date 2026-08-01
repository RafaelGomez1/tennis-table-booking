package com.codely.member.application.search

import arrow.core.raise.Raise
import com.codely.member.domain.*

context(MemberRepository, Raise<SearchMembersError>)
suspend fun handle(query: SearchMembersQuery): Page<Member> {
    val memberType = query.type?.let { MemberType.fromString(it) ?: raise(SearchMembersError.InvalidType) }
    val criteria = memberType?.let { MemberSearchByCriteria.ByType(it) } ?: MemberSearchByCriteria.All
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
