package com.codely.departures.domain

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

data class Departure(
    val id: DepartureId,
    val memberId: MemberId,
    val name: MemberName,
    val surname: MemberSurname,
    val phoneNumbers: ContactPhoneNumbers,
    val type: Membership,
    val departureDate: DepartureDate,
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
        fun fromMember(member: Member, departureDate: DepartureDate): Departure =
            Departure(
                id = DepartureId.generate(),
                memberId = member.id,
                name = member.name,
                surname = member.surname,
                phoneNumbers = member.phoneNumbers,
                type = member.type,
                idNumber = member.idNumber,
                address = member.address,
                city = member.city,
                postalCode = member.postalCode,
                dateOfBirth = member.dateOfBirth,
                email = member.email,
                memberSince = member.memberSince,
                ageGroup = member.ageGroup,
                departureDate = departureDate
            )
    }
}
