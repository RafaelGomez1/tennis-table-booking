package com.codely.member.domain

data class Page<T>(
    val content: List<T>,
    val page: Int,
    val size: Int,
    val totalElements: Long
) {
    val totalPages: Int get() = if (size > 0) ((totalElements + size - 1) / size).toInt() else 0
}

data class PageRequest(val page: Int = 0, val size: Int = 20)

interface MemberRepository {
    suspend fun save(member: Member)
    suspend fun find(criteria: MemberFindByCriteria): Member?
    suspend fun search(criteria: MemberSearchByCriteria, pageRequest: PageRequest): Page<Member>
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
