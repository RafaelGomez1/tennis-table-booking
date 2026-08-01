package com.codely.member

import com.codely.member.domain.ContactPhoneNumbers
import com.codely.member.domain.Member
import com.codely.member.domain.MemberId
import com.codely.member.domain.MemberName
import com.codely.member.domain.MemberSurname
import com.codely.member.domain.MemberType

object MemberMother {

    fun random(
        id: MemberId = MemberIdMother.random(),
        name: MemberName = MemberNameMother.random(),
        surname: MemberSurname = MemberSurnameMother.random(),
        phoneNumbers: ContactPhoneNumbers = ContactPhoneNumbersMother.random(),
        type: MemberType = MemberTypeMother.random()
    ): Member = Member(
        id = id,
        name = name,
        surname = surname,
        phoneNumbers = phoneNumbers,
        type = type
    )

    fun casual(
        id: MemberId = MemberIdMother.random(),
        name: MemberName = MemberNameMother.random(),
        surname: MemberSurname = MemberSurnameMother.random(),
        phoneNumbers: ContactPhoneNumbers = ContactPhoneNumbersMother.random()
    ): Member = random(id = id, name = name, surname = surname, phoneNumbers = phoneNumbers, type = MemberTypeMother.casual())

    fun academyBeginner(
        id: MemberId = MemberIdMother.random(),
        name: MemberName = MemberNameMother.random(),
        surname: MemberSurname = MemberSurnameMother.random(),
        phoneNumbers: ContactPhoneNumbers = ContactPhoneNumbersMother.random(),
        type: MemberType = MemberTypeMother.academyBeginner()
    ): Member = random(id = id, name = name, surname = surname, phoneNumbers = phoneNumbers, type = type)

    fun academyIntermediate(
        id: MemberId = MemberIdMother.random(),
        name: MemberName = MemberNameMother.random(),
        surname: MemberSurname = MemberSurnameMother.random(),
        phoneNumbers: ContactPhoneNumbers = ContactPhoneNumbersMother.random()
    ): Member = random(id = id, name = name, surname = surname, phoneNumbers = phoneNumbers, type = MemberTypeMother.academyIntermediate())

    fun competition(
        id: MemberId = MemberIdMother.random(),
        name: MemberName = MemberNameMother.random(),
        surname: MemberSurname = MemberSurnameMother.random(),
        phoneNumbers: ContactPhoneNumbers = ContactPhoneNumbersMother.random(),
        type: MemberType = MemberTypeMother.competition()
    ): Member = random(id = id, name = name, surname = surname, phoneNumbers = phoneNumbers, type = type)
}
