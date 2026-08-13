package com.codely.members.application.search

import com.codely.members.domain.Member
import com.codely.members.domain.MemberRepository
import com.codely.members.domain.MemberSearchByCriteria
import com.codely.members.domain.Page
import com.codely.members.domain.PageRequest

context(MemberRepository)
suspend fun searchMembers(criteria: MemberSearchByCriteria, pageRequest: PageRequest): Page<Member> =
    search(criteria, pageRequest)
