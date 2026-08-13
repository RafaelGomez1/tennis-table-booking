package com.codely.members.fakes

import com.codely.members.domain.Member
import com.codely.members.domain.MemberFindByCriteria
import com.codely.members.domain.MemberFindByCriteria.ById
import com.codely.members.domain.MemberId
import com.codely.members.domain.MemberRepository
import com.codely.members.domain.MemberSearchByCriteria
import com.codely.members.domain.MemberSearchByCriteria.All
import com.codely.members.domain.MemberSearchByCriteria.ByGroup
import com.codely.members.domain.MemberSearchByCriteria.ByType
import com.codely.members.domain.Membership
import com.codely.members.domain.Page
import com.codely.members.domain.PageRequest
import com.codely.shared.fakes.FakeRepository

class FakeMemberRepository : MemberRepository, FakeRepository<MemberId, Member> {
    override val elements = mutableMapOf<MemberId, Member>()
    override val errors = mutableListOf<Throwable>()

    override suspend fun save(member: Member) {
        elements.saveOrUpdate(member, member.id)
    }

    override suspend fun find(criteria: MemberFindByCriteria): Member? =
        when (criteria) {
            is ById -> elements[criteria.id]
        }

    override suspend fun search(criteria: MemberSearchByCriteria, pageRequest: PageRequest): Page<Member> {
        val all = when (criteria) {
            is All -> elements.values.toList()
            is ByGroup -> elements.values.filter {
                val type = it.type
                type is Membership.AcademyBeginner && type.groups.contains(criteria.group)
            }
            is ByType -> elements.values.filter {
                it.type::class == criteria.type::class
            }
        }
        val offset = pageRequest.page * pageRequest.size
        val paged = all.drop(offset).take(pageRequest.size)

        return Page(
            content = paged,
            page = pageRequest.page,
            size = pageRequest.size,
            totalElements = all.size.toLong()
        )
    }

    override suspend fun delete(id: MemberId) {
        elements.remove(id)
    }
}
