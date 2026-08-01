package com.codely.member

import com.codely.member.domain.AcademyGroup
import com.codely.member.domain.ContactPhoneNumber
import com.codely.member.domain.ContactPhoneNumbers
import com.codely.member.domain.MemberId
import com.codely.member.domain.MemberName
import com.codely.member.domain.MemberSurname
import com.codely.member.domain.MemberType
import com.codely.member.domain.Team
import java.util.UUID

object MemberTypeMother {

    fun random(): MemberType = listOf(casual(), academyBeginner(), academyIntermediate(), competition()).random()

    fun casual(): MemberType = MemberType.Casual

    fun academyBeginner(
        group: AcademyGroup = AcademyGroup.values().random()
    ): MemberType = MemberType.AcademyBeginner(group)

    fun academyIntermediate(): MemberType = MemberType.AcademyIntermediate

    fun competition(
        team: Team = Team.values().random()
    ): MemberType = MemberType.Competition(team)
}

object MemberIdMother {
    fun random(): MemberId = MemberId(UUID.randomUUID())
}

object MemberNameMother {
    private val names = listOf("Carlos", "Maria", "Pedro", "Laura", "Juan", "Ana")

    fun random(): MemberName = MemberName(names.random())
}

object MemberSurnameMother {
    private val surnames = listOf("Garcia", "Lopez", "Martinez", "Fernandez", "Gonzalez", "Rodriguez")

    fun random(): MemberSurname = MemberSurname(surnames.random())
}

object ContactPhoneNumbersMother {
    fun random(): ContactPhoneNumbers =
        ContactPhoneNumbers.of(
            ContactPhoneNumber("6${(10000000..99999999).random()}"),
            ContactPhoneNumber("6${(10000000..99999999).random()}")
        )

    fun single(): ContactPhoneNumbers =
        ContactPhoneNumbers.of(ContactPhoneNumber("6${(10000000..99999999).random()}"))
}
