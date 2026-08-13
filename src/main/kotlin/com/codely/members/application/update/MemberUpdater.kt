package com.codely.members.application.update

import arrow.core.raise.Raise
import com.codely.members.application.update.UpdateMemberError.MemberNotFound
import com.codely.members.domain.ContactPhoneNumbers
import com.codely.members.domain.IDNumber
import com.codely.members.domain.Member
import com.codely.members.domain.MemberAddress
import com.codely.members.domain.MemberCity
import com.codely.members.domain.MemberDateOfBirth
import com.codely.members.domain.MemberEmail
import com.codely.members.domain.MemberFindByCriteria.ById
import com.codely.members.domain.MemberId
import com.codely.members.domain.MemberName
import com.codely.members.domain.MemberPostalCode
import com.codely.members.domain.MemberRepository
import com.codely.members.domain.MemberSince
import com.codely.members.domain.MemberSurname
import com.codely.members.domain.Membership

context(MemberRepository, Raise<UpdateMemberError>)
suspend fun updateMember(
    id: MemberId,
    name: MemberName,
    surname: MemberSurname,
    phoneNumbers: ContactPhoneNumbers,
    type: Membership,
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
