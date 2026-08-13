package com.codely.members.infrastructure.database.document

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface JpaMemberRepository : CoroutineCrudRepository<MemberDocument, String> {
    suspend fun findAllByType(type: String): List<MemberDocument>
}
