package com.codely.member.application.search

import com.codely.member.domain.Member
import com.codely.member.domain.MemberRepository
import com.codely.member.domain.MemberSearchByCriteria
import com.codely.member.domain.Page
import com.codely.member.domain.PageRequest

context(MemberRepository)
suspend fun searchMembers(criteria: MemberSearchByCriteria, pageRequest: PageRequest): Page<Member> =
    search(criteria, pageRequest)
