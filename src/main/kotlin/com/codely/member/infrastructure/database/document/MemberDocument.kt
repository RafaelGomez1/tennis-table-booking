package com.codely.member.infrastructure.database.document

import com.codely.member.domain.AcademyGroup
import com.codely.member.domain.ContactPhoneNumber
import com.codely.member.domain.ContactPhoneNumbers
import com.codely.member.domain.Member
import com.codely.member.domain.MemberId
import com.codely.member.domain.MemberName
import com.codely.member.domain.MemberSurname
import com.codely.member.domain.MemberType
import com.codely.member.domain.Team
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
    val academyGroup: String? = null,
    val team: String? = null
) {

    fun toMember(): Member = Member(
        id = MemberId(UUID.fromString(id)),
        name = MemberName(name),
        surname = MemberSurname(surname),
        phoneNumbers = ContactPhoneNumbers(phoneNumbers.map { ContactPhoneNumber(it) }),
        type = toMemberType()
    )

    private fun toMemberType(): MemberType = when (type) {
        "CASUAL" -> MemberType.Casual
        "ACADEMY_BEGINNER" -> MemberType.AcademyBeginner(AcademyGroup.valueOf(academyGroup!!))
        "ACADEMY_INTERMEDIATE" -> MemberType.AcademyIntermediate
        "COMPETITION" -> MemberType.Competition(Team.valueOf(team!!))
        else -> throw IllegalStateException("Unknown member type: $type")
    }
}

internal fun Member.toDocument(): MemberDocument = MemberDocument(
    id = id.value.toString(),
    name = name.value,
    surname = surname.value,
    phoneNumbers = phoneNumbers.values.map { it.value },
    type = type.toName(),
    academyGroup = (type as? MemberType.AcademyBeginner)?.group?.name,
    team = (type as? MemberType.Competition)?.team?.name
)
