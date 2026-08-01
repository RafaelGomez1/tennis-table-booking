package com.codely.member.domain

interface MemberRepository {
    suspend fun save(member: Member)
    suspend fun find(criteria: MemberFindByCriteria): Member?
    suspend fun search(criteria: MemberSearchByCriteria): List<Member>
}

sealed class MemberFindByCriteria {
    class ById(val id: MemberId) : MemberFindByCriteria()
}

sealed class MemberSearchByCriteria {
    data object All : MemberSearchByCriteria()
    class ByGroup(val group: AcademyGroup) : MemberSearchByCriteria()
    class ByType(val type: MemberType) : MemberSearchByCriteria()
}

suspend fun MemberRepository.save(member: Member): Member =
    save(member).let { member }
