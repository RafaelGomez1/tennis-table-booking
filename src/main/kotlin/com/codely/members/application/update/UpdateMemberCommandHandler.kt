package com.codely.members.application.update

import arrow.core.raise.Raise
import arrow.core.raise.catch
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
import com.codely.members.domain.MemberRepository
import com.codely.members.domain.MemberSince
import com.codely.members.domain.MemberSurname
import com.codely.members.domain.Membership
import java.time.LocalDate
import java.util.UUID

context(MemberRepository, Raise<UpdateMemberError>)
suspend fun handle(command: UpdateMemberCommand) {
    val id = catch({ MemberId(UUID.fromString(command.id)) }) { raise(UpdateMemberError.InvalidUUID) }
    val name = catch({ MemberName(command.name) }) { raise(UpdateMemberError.InvalidName) }
    val surname = catch({ MemberSurname(command.surname) }) { raise(UpdateMemberError.InvalidSurname) }
    val phoneNumbers = catch({
        ContactPhoneNumbers(command.phoneNumbers.map { ContactPhoneNumber(it) })
    }) { raise(UpdateMemberError.InvalidPhoneNumbers) }
    val type = catch({ Membership.fromString(command.type, command.academyGroups, command.team) }) { raise(UpdateMemberError.InvalidType) }
    val idNumber = command.idNumber?.let { catch({ IDNumber(it) }) { raise(UpdateMemberError.InvalidIDNumber) } }
    val address = command.address?.let { catch({ MemberAddress(it) }) { raise(UpdateMemberError.InvalidAddress) } }
    val city = command.city?.let { catch({ MemberCity(it) }) { raise(UpdateMemberError.InvalidCity) } }
    val postalCode = command.postalCode?.let { catch({ MemberPostalCode(it) }) { raise(UpdateMemberError.InvalidPostalCode) } }
    val dateOfBirth = command.dateOfBirth?.let { catch({ MemberDateOfBirth(LocalDate.parse(it)) }) { raise(UpdateMemberError.InvalidDateOfBirth) } }
    val email = command.email?.let { catch({ MemberEmail(it) }) { raise(UpdateMemberError.InvalidEmail) } }
    val memberSince = command.memberSince?.let { catch({ MemberSince(LocalDate.parse(it)) }) { raise(UpdateMemberError.InvalidMemberSince) } }

    updateMember(
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
}

data class UpdateMemberCommand(
    val id: String,
    val name: String,
    val surname: String,
    val phoneNumbers: List<String>,
    val type: String,
    val academyGroups: List<String>? = null,
    val team: String? = null,
    val idNumber: String? = null,
    val address: String? = null,
    val city: String? = null,
    val postalCode: String? = null,
    val dateOfBirth: String? = null,
    val email: String? = null,
    val memberSince: String? = null
)

sealed class UpdateMemberError {
    data object InvalidUUID : UpdateMemberError()
    data object MemberNotFound : UpdateMemberError()
    data object InvalidName : UpdateMemberError()
    data object InvalidSurname : UpdateMemberError()
    data object InvalidPhoneNumbers : UpdateMemberError()
    data object InvalidType : UpdateMemberError()
    data object InvalidIDNumber : UpdateMemberError()
    data object InvalidAddress : UpdateMemberError()
    data object InvalidCity : UpdateMemberError()
    data object InvalidPostalCode : UpdateMemberError()
    data object InvalidDateOfBirth : UpdateMemberError()
    data object InvalidEmail : UpdateMemberError()
    data object InvalidMemberSince : UpdateMemberError()
}
