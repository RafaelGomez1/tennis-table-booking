package com.codely.member.primaryadapter.rest

import com.codely.member.domain.Member
import com.codely.member.domain.Page

data class MemberResponseDTO(
    val id: String,
    val name: String,
    val surname: String,
    val phoneNumbers: List<String>,
    val type: String,
    val academyGroup: String?,
    val team: String?,
    val memberCode: String?,
    val idNumber: String?,
    val address: String?,
    val city: String?,
    val postalCode: String?,
    val dateOfBirth: String?,
    val email: String?,
    val memberSince: String?,
    val age: Int?
) {
    companion object {
        fun fromDomain(member: Member): MemberResponseDTO =
            MemberResponseDTO(
                id = member.id.value.toString(),
                name = member.name.value,
                surname = member.surname.value,
                phoneNumbers = member.phoneNumbers.values.map { it.value },
                type = member.type.toName(),
                academyGroup = member.type.academyGroup(),
                team = member.type.team(),
                memberCode = member.memberCode?.value,
                idNumber = member.idNumber?.value,
                address = member.address?.value,
                city = member.city?.value,
                postalCode = member.postalCode?.value,
                dateOfBirth = member.dateOfBirth?.value?.toString(),
                email = member.email?.value,
                memberSince = member.memberSince?.value?.toString(),
                age = member.age
            )

        fun fromDomain(page: Page<Member>): PageDTO<MemberResponseDTO> =
            PageDTO(
                content = page.content.map { fromDomain(it) },
                page = page.page,
                size = page.size,
                totalElements = page.totalElements,
                totalPages = page.totalPages
            )
    }
}
