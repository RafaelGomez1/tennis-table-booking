package com.codely.member.application.update

import arrow.core.raise.Raise
import com.codely.member.application.update.UpdateMemberError.MemberNotFound
import com.codely.member.domain.ContactPhoneNumbers
import com.codely.member.domain.Member
import com.codely.member.domain.MemberFindByCriteria
import com.codely.member.domain.MemberFindByCriteria.ById
import com.codely.member.domain.MemberId
import com.codely.member.domain.MemberName
import com.codely.member.domain.MemberRepository
import com.codely.member.domain.MemberSurname
import com.codely.member.domain.MemberType

context(MemberRepository, Raise<UpdateMemberError>)
suspend fun updateMember(
    id: MemberId,
    name: MemberName,
    surname: MemberSurname,
    phoneNumbers: ContactPhoneNumbers,
    type: MemberType
) {
    find(ById(id)) ?: raise(MemberNotFound)

    val updatedMember = Member(id = id, name = name, surname = surname, phoneNumbers = phoneNumbers, type = type)

    save(updatedMember)
}
