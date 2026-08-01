package com.codely.member.domain

data class Member(
    val id: MemberId,
    val name: MemberName,
    val surname: MemberSurname,
    val phoneNumbers: ContactPhoneNumbers,
    val type: MemberType
) {
    companion object {
        fun create(
            name: MemberName,
            surname: MemberSurname,
            phoneNumbers: ContactPhoneNumbers,
            type: MemberType
        ): Member = Member(
            id = MemberId.generate(),
            name = name,
            surname = surname,
            phoneNumbers = phoneNumbers,
            type = type
        )
    }
}
