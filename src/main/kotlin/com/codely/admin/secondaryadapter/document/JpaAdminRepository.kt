package com.codely.admin.secondaryadapter.document

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface JpaAdminRepository : CoroutineCrudRepository<AdminDocument, String> {
    suspend fun findByUsername(username: String): AdminDocument?
    suspend fun findByAccessKey(accessKey: String): AdminDocument?
}
