package com.codely.members.infrastructure.database.document

import com.codely.members.domain.AcademyGroup
import com.codely.members.domain.AgeGroup
import com.codely.members.domain.ContactPhoneNumber
import com.codely.members.domain.ContactPhoneNumbers
import com.codely.members.domain.IDNumber
import com.codely.members.domain.Member
import com.codely.members.domain.MemberAddress
import com.codely.members.domain.MemberCity
import com.codely.members.domain.MemberDateOfBirth
import com.codely.members.domain.MemberEmail
import com.codely.members.domain.MemberId
import com.codely.members.domain.MemberName
import com.codely.members.domain.MemberPostalCode
import com.codely.members.domain.MemberSince
import com.codely.members.domain.MemberSurname
import com.codely.members.domain.Membership
import com.codely.members.domain.Team
import java.time.LocalDate
import java.util.UUID
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "Members")
class MemberDocument(
    @Id
    val id: String,
    val name: String,
    val surname: String,
    val phoneNumbers: List<String>,
    val type: String,
    val academyGroups: List<String> = emptyList(),
    val team: String? = null,
    val idNumber: String? = null,
    val address: String? = null,
    val city: String? = null,
    val postalCode: String? = null,
    val dateOfBirth: String? = null,
    val email: String? = null,
    val memberSince: String? = null,
    val ageGroup: String? = null
) {

    fun toMember(): Member {
        val dateOfBirth = dateOfBirth?.let { MemberDateOfBirth(LocalDate.parse(it)) }
        val resolvedAgeGroup = ageGroup?.let { AgeGroup.valueOf(it) }
            ?: dateOfBirth?.age()?.let { AgeGroup.fromAge(it) }

        return Member(
            id = MemberId(UUID.fromString(id)),
            name = MemberName(name),
            surname = MemberSurname(surname),
            phoneNumbers = ContactPhoneNumbers(phoneNumbers.map { ContactPhoneNumber(it) }),
            type = toMemberType(),
            idNumber = idNumber?.let { IDNumber(it) },
            address = address?.let { MemberAddress(it) },
            city = city?.let { MemberCity(it) },
            postalCode = postalCode?.let { MemberPostalCode(it) },
            dateOfBirth = dateOfBirth,
            email = email?.let { MemberEmail(it) },
            memberSince = memberSince?.let { MemberSince(LocalDate.parse(it)) },
            ageGroup = resolvedAgeGroup
        )
    }

    private fun toMemberType(): Membership = when (type) {
        "CASUAL" -> Membership.Casual()
        "ACADEMY_BEGINNER" -> Membership.AcademyBeginner(academyGroups.map { AcademyGroup.valueOf(it) })
        "ACADEMY_INTERMEDIATE" -> Membership.AcademyIntermediate()
        "COMPETITION" -> Membership.Competition(Team.valueOf(team!!))
        "COACH" -> Membership.Coach()
        else -> throw IllegalStateException("Unknown member type: $type")
    }
}

internal fun Member.toDocument(): MemberDocument = MemberDocument(
    id = id.value.toString(),
    name = name.value,
    surname = surname.value,
    phoneNumbers = phoneNumbers.values.map { it.value },
    type = type.toName(),
    academyGroups = type.academyGroups(),
    team = type.team(),
    idNumber = idNumber?.value,
    address = address?.value,
    city = city?.value,
    postalCode = postalCode?.value,
    dateOfBirth = dateOfBirth?.value?.toString(),
    email = email?.value,
    memberSince = memberSince?.value?.toString(),
    ageGroup = resolvedAgeGroup()?.name
)
