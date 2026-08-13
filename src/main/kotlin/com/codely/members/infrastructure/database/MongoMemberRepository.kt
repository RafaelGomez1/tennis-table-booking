package com.codely.members.infrastructure.database

import com.codely.members.domain.Member
import com.codely.members.domain.MemberFindByCriteria
import com.codely.members.domain.MemberFindByCriteria.ById
import com.codely.members.domain.MemberId
import com.codely.members.domain.MemberRepository
import com.codely.members.domain.MemberSearchByCriteria
import com.codely.members.domain.MemberSearchByCriteria.All
import com.codely.members.domain.MemberSearchByCriteria.ByGroup
import com.codely.members.domain.MemberSearchByCriteria.ByType
import com.codely.members.domain.Page
import com.codely.members.domain.PageRequest
import com.codely.members.infrastructure.database.document.JpaMemberRepository
import com.codely.members.infrastructure.database.document.toDocument
import com.codely.shared.dispatcher.withIOContext
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Component

@Component
class MongoMemberRepository(private val repository: JpaMemberRepository) : MemberRepository {

    override suspend fun save(member: Member) {
        withIOContext {
            repository.save(member.toDocument())
        }
    }

    override suspend fun find(criteria: MemberFindByCriteria): Member? =
        withIOContext {
            when (criteria) {
                is ById -> repository.findById(criteria.id.value.toString())
            }?.toMember()
        }

    override suspend fun search(criteria: MemberSearchByCriteria, pageRequest: PageRequest): Page<Member> =
        withIOContext {
            val allDocuments = when (criteria) {
                is All -> repository.findAll().toList()
                is ByGroup -> repository.findAllByType("ACADEMY_BEGINNER")
                is ByType -> repository.findAllByType(criteria.type.toName())
            }
            val totalElements = allDocuments.size.toLong()
            val offset = pageRequest.page * pageRequest.size
            val pagedDocuments = allDocuments.drop(offset).take(pageRequest.size)

            Page(
                content = pagedDocuments.map { it.toMember() },
                page = pageRequest.page,
                size = pageRequest.size,
                totalElements = totalElements
            )
        }

    override suspend fun delete(id: MemberId) {
        withIOContext {
            repository.deleteById(id.value.toString())
        }
    }
}
