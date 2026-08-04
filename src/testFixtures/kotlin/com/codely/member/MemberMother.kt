package com.codely.member

import com.codely.member.domain.AgeGroup
import com.codely.member.domain.ContactPhoneNumbers
import com.codely.member.domain.IDNumber
import com.codely.member.domain.Member
import com.codely.member.domain.MemberAddress
import com.codely.member.domain.MemberCity
import com.codely.member.domain.MemberCode
import com.codely.member.domain.MemberDateOfBirth
import com.codely.member.domain.MemberEmail
import com.codely.member.domain.MemberId
import com.codely.member.domain.MemberName
import com.codely.member.domain.MemberPostalCode
import com.codely.member.domain.MemberSince
import com.codely.member.domain.MemberSurname
import com.codely.member.domain.MemberType

object MemberMother {

    fun random(
        id: MemberId = MemberIdMother.random(),
        name: MemberName = MemberNameMother.random(),
        surname: MemberSurname = MemberSurnameMother.random(),
        phoneNumbers: ContactPhoneNumbers = ContactPhoneNumbersMother.random(),
        type: MemberType = MemberTypeMother.random(),
        memberCode: MemberCode? = null,
        idNumber: IDNumber? = null,
        address: MemberAddress? = null,
        city: MemberCity? = null,
        postalCode: MemberPostalCode? = null,
        dateOfBirth: MemberDateOfBirth? = null,
        email: MemberEmail? = null,
        memberSince: MemberSince? = null,
        ageGroup: AgeGroup? = null
    ): Member = Member(
        id = id,
        name = name,
        surname = surname,
        phoneNumbers = phoneNumbers,
        type = type,
        memberCode = memberCode,
        idNumber = idNumber,
        address = address,
        city = city,
        postalCode = postalCode,
        dateOfBirth = dateOfBirth,
        email = email,
        memberSince = memberSince,
        ageGroup = ageGroup
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
