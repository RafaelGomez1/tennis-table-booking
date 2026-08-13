package com.codely.members.application.search

import arrow.core.raise.Raise
import com.codely.members.domain.Member
import com.codely.members.domain.MemberRepository
import com.codely.members.domain.MemberSearchByCriteria
import com.codely.members.domain.MemberSearchByCriteria.All
import com.codely.members.domain.Membership
import com.codely.members.domain.Page
import com.codely.members.domain.PageRequest

context(MemberRepository, Raise<SearchMembersError>)
suspend fun handle(query: SearchMembersQuery): Page<Member> {
    val membership = query.type?.let { Membership.fromFilter(it) ?: raise(SearchMembersError.InvalidType) }
    val criteria = membership?.let { MemberSearchByCriteria.ByType(it) } ?: All
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
