package com.codely.member

import com.codely.member.domain.AcademyGroup
import com.codely.member.domain.ContactPhoneNumber
import com.codely.member.domain.ContactPhoneNumbers
import com.codely.member.domain.IDNumber
import com.codely.member.domain.MemberAddress
import com.codely.member.domain.MemberCity
import com.codely.member.domain.MemberDateOfBirth
import com.codely.member.domain.MemberEmail
import com.codely.member.domain.MemberId
import com.codely.member.domain.MemberName
import com.codely.member.domain.MemberPostalCode
import com.codely.member.domain.MemberSince
import com.codely.member.domain.MemberSurname
import com.codely.member.domain.MemberType
import com.codely.member.domain.Team
import java.time.LocalDate
import java.util.UUID

object MemberTypeMother {

    fun random(): MemberType = listOf(casual(), academyBeginner(), academyIntermediate(), competition()).random()

    fun casual(): MemberType = MemberType.Casual

    fun academyBeginner(
        group: AcademyGroup = AcademyGroup.values().random()
    ): MemberType = MemberType.AcademyBeginner(listOf(group))

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

object IDNumberMother {
    fun random(): IDNumber = IDNumber("${(10000000..99999999).random()}A")
}

object MemberAddressMother {
    private val addresses = listOf("Calle Mayor 1", "Av. de la Constitución 5", "Plaza España 3")
    fun random(): MemberAddress = MemberAddress(addresses.random())
}

object MemberCityMother {
    private val cities = listOf("Madrid", "Barcelona", "Valencia", "Sevilla")
    fun random(): MemberCity = MemberCity(cities.random())
}

object MemberPostalCodeMother {
    fun random(): MemberPostalCode = MemberPostalCode("${(10000..52999).random()}")
}

object MemberDateOfBirthMother {
    fun random(): MemberDateOfBirth = MemberDateOfBirth(
        LocalDate.of((1970..2005).random(), (1..12).random(), (1..28).random())
    )
}

object MemberEmailMother {
    fun random(): MemberEmail = MemberEmail("member${(100..999).random()}@example.com")
}

object MemberSinceMother {
    fun random(): MemberSince = MemberSince(
        LocalDate.of((2015..2025).random(), (1..12).random(), (1..28).random())
    )
}
