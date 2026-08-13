package com.codely.members

import com.codely.members.domain.AcademyGroup
import com.codely.members.domain.ContactPhoneNumber
import com.codely.members.domain.ContactPhoneNumbers
import com.codely.members.domain.IDNumber
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

object MemberTypeMother {

    fun random(): Membership = listOf(casual(), academyBeginner(), academyIntermediate(), competition()).random()

    fun casual(): Membership = Membership.Casual

    fun academyBeginner(
        group: AcademyGroup = AcademyGroup.values().random()
    ): Membership = Membership.AcademyBeginner(listOf(group))

    fun academyIntermediate(): Membership = Membership.AcademyIntermediate

    fun competition(
        team: Team = Team.values().random()
    ): Membership = Membership.Competition(team)
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
