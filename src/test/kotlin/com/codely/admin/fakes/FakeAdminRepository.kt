package com.codely.admin.fakes

import com.codely.admin.domain.AccessKey
import com.codely.admin.domain.Admin
import com.codely.admin.domain.AdminFindByCriteria
import com.codely.admin.domain.AdminFindByCriteria.ByKey
import com.codely.admin.domain.AdminRepository
import com.codely.admin.domain.Password
import com.codely.admin.domain.Username
import com.codely.shared.fakes.FakeRepository
import java.util.*

class FakeAdminRepository : AdminRepository, FakeRepository<UUID, Admin> {
    override val elements = mutableMapOf<UUID, Admin>()
    override val errors = mutableListOf<Throwable>()

    override suspend fun save(admin: Admin) { elements.saveOrUpdate(admin, admin.id) }

    override suspend fun find(criteria: AdminFindByCriteria): Admin? =
        when (criteria) {
            is ByKey -> elements.values.firstOrNull { it.key == criteria.accessKey }
        }

    override suspend fun signIn(username: Username, password: Password): AccessKey? =
        elements.values.firstOrNull { it.username == username && it.password == password }?.key
}
