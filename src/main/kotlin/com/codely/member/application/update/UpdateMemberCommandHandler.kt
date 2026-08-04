package com.codely.member.application.update

import arrow.core.raise.Raise
import arrow.core.raise.catch
import com.codely.member.domain.ContactPhoneNumber
import com.codely.member.domain.ContactPhoneNumbers
import com.codely.member.domain.IDNumber
import com.codely.member.domain.MemberAddress
import com.codely.member.domain.MemberCity
import com.codely.member.domain.MemberDateOfBirth
import com.codely.member.domain.MemberEmail
import com.codely.member.domain.MemberId
import com.codely.member.domain.MemberName
import com.codely.member.domain.MemberPostalCode
import com.codely.member.domain.MemberRepository
import com.codely.member.domain.MemberSince
import com.codely.member.domain.MemberSurname
import com.codely.member.domain.MemberType
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
    val type = catch({ MemberType.fromString(command.type, command.academyGroup, command.team) }) { raise(UpdateMemberError.InvalidType) }
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
    val academyGroup: String? = null,
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
