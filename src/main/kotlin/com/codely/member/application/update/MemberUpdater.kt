package com.codely.member.application.update

import arrow.core.raise.Raise
import com.codely.member.application.update.UpdateMemberError.MemberNotFound
import com.codely.member.domain.ContactPhoneNumbers
import com.codely.member.domain.IDNumber
import com.codely.member.domain.Member
import com.codely.member.domain.MemberAddress
import com.codely.member.domain.MemberCity
import com.codely.member.domain.MemberCode
import com.codely.member.domain.MemberDateOfBirth
import com.codely.member.domain.MemberEmail
import com.codely.member.domain.MemberFindByCriteria.ById
import com.codely.member.domain.MemberId
import com.codely.member.domain.MemberName
import com.codely.member.domain.MemberPostalCode
import com.codely.member.domain.MemberRepository
import com.codely.member.domain.MemberSince
import com.codely.member.domain.MemberSurname
import com.codely.member.domain.MemberType

context(MemberRepository, Raise<UpdateMemberError>)
suspend fun updateMember(
    id: MemberId,
    name: MemberName,
    surname: MemberSurname,
    phoneNumbers: ContactPhoneNumbers,
    type: MemberType,
    memberCode: MemberCode? = null,
    idNumber: IDNumber? = null,
    address: MemberAddress? = null,
    city: MemberCity? = null,
    postalCode: MemberPostalCode? = null,
    dateOfBirth: MemberDateOfBirth? = null,
    email: MemberEmail? = null,
    memberSince: MemberSince? = null
) {
    find(ById(id)) ?: raise(MemberNotFound)

    val updatedMember = Member(
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
        memberSince = memberSince
    )

    save(updatedMember)
}
