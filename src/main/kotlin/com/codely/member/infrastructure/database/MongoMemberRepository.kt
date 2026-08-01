package com.codely.member.infrastructure.database

import com.codely.member.domain.Member
import com.codely.member.domain.MemberFindByCriteria
import com.codely.member.domain.MemberFindByCriteria.ById
import com.codely.member.domain.MemberId
import com.codely.member.domain.MemberRepository
import com.codely.member.domain.MemberSearchByCriteria
import com.codely.member.domain.MemberSearchByCriteria.All
import com.codely.member.domain.MemberSearchByCriteria.ByGroup
import com.codely.member.domain.MemberSearchByCriteria.ByType
import com.codely.member.domain.Page
import com.codely.member.domain.PageRequest
import com.codely.member.infrastructure.database.document.JpaMemberRepository
import com.codely.member.infrastructure.database.document.toDocument
import com.codely.member.infrastructure.database.document.toDocumentType
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
                is ByType -> repository.findAllByType(criteria.type.toDocumentType())
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
