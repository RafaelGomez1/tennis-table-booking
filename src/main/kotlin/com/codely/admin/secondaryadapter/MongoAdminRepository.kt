package com.codely.admin.secondaryadapter

import com.codely.admin.domain.*
import com.codely.admin.domain.AdminFindByCriteria.ByKey
import com.codely.admin.secondaryadapter.document.JpaAdminRepository
import com.codely.admin.secondaryadapter.document.toDocument
import com.codely.shared.dispatcher.withIOContext
import org.mindrot.jbcrypt.BCrypt
import org.springframework.stereotype.Component

@Component
class MongoAdminRepository(private val repository: JpaAdminRepository) : AdminRepository {

    override suspend fun save(admin: Admin) {
        withIOContext {
            repository.save(admin.toDocument())
            }
        }

    override suspend fun find(criteria: AdminFindByCriteria): Admin? =
        withIOContext {
            when (criteria) {
                is ByKey -> repository.findByAccessKey(criteria.accessKey.value)
            }?.toAdmin()
        }

    override suspend fun signIn(username: Username, password: Password): AccessKey? =
        withIOContext {
            repository.findByUsername(username.value)
                ?.let { admin ->
                    if (BCrypt.checkpw(password.value, admin.password)) AccessKey(admin.accessKey)
                    else null
                }
        }
}
