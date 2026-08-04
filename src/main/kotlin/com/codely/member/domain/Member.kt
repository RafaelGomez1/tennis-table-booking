package com.codely.member.domain

data class Member(
    val id: MemberId,
    val name: MemberName,
    val surname: MemberSurname,
    val phoneNumbers: ContactPhoneNumbers,
    val type: MemberType,
    val idNumber: IDNumber? = null,
    val address: MemberAddress? = null,
    val city: MemberCity? = null,
    val postalCode: MemberPostalCode? = null,
    val dateOfBirth: MemberDateOfBirth? = null,
    val email: MemberEmail? = null,
    val memberSince: MemberSince? = null,
    val ageGroup: AgeGroup? = null
) {
    val age: Int? get() = dateOfBirth?.age()

    fun resolvedAgeGroup(): AgeGroup? = ageGroup ?: age?.let { AgeGroup.fromAge(it) }

    companion object {
        fun create(
            name: MemberName,
            surname: MemberSurname,
            phoneNumbers: ContactPhoneNumbers,
            type: MemberType,
            idNumber: IDNumber? = null,
            address: MemberAddress? = null,
            city: MemberCity? = null,
            postalCode: MemberPostalCode? = null,
            dateOfBirth: MemberDateOfBirth? = null,
            email: MemberEmail? = null,
            memberSince: MemberSince? = null
        ): Member = Member(
            id = MemberId.generate(),
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
    }
}
