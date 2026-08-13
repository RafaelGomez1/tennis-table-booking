package com.codely.members

import com.codely.members.domain.AgeGroup
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

object MemberMother {

    fun random(
        id: MemberId = MemberIdMother.random(),
        name: MemberName = MemberNameMother.random(),
        surname: MemberSurname = MemberSurnameMother.random(),
        phoneNumbers: ContactPhoneNumbers = ContactPhoneNumbersMother.random(),
        type: Membership = MemberTypeMother.random(),
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
        type: Membership = MemberTypeMother.academyBeginner()
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
        type: Membership = MemberTypeMother.competition()
    ): Member = random(id = id, name = name, surname = surname, phoneNumbers = phoneNumbers, type = type)
}
