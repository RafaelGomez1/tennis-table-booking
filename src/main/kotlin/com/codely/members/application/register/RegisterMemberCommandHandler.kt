package com.codely.members.application.register

import arrow.core.raise.Raise
import arrow.core.raise.catch
import com.codely.members.domain.ContactPhoneNumber
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
import com.codely.members.domain.MemberRepository
import com.codely.members.domain.MemberSince
import com.codely.members.domain.MemberSurname
import com.codely.members.domain.Membership
import java.time.LocalDate
import java.util.UUID

context(MemberRepository, Raise<RegisterMemberError>)
suspend fun handle(command: RegisterMemberCommand) {
    val id = catch({ MemberId(UUID.fromString(command.id)) }) { raise(RegisterMemberError.InvalidUUID) }
    val name = catch({ MemberName(command.name) }) { raise(RegisterMemberError.InvalidName) }
    val surname = catch({ MemberSurname(command.surname) }) { raise(RegisterMemberError.InvalidSurname) }
    val phoneNumbers = catch({
        ContactPhoneNumbers(command.phoneNumbers.map { ContactPhoneNumber(it) })
    }) { raise(RegisterMemberError.InvalidPhoneNumbers) }
    val type = command.toMemberType()
    val idNumber = command.idNumber?.let { catch({ IDNumber(it) }) { raise(RegisterMemberError.InvalidIDNumber) } }
    val address = command.address?.let { catch({ MemberAddress(it) }) { raise(RegisterMemberError.InvalidAddress) } }
    val city = command.city?.let { catch({ MemberCity(it) }) { raise(RegisterMemberError.InvalidCity) } }
    val postalCode = command.postalCode?.let { catch({ MemberPostalCode(it) }) { raise(RegisterMemberError.InvalidPostalCode) } }
    val dateOfBirth = command.dateOfBirth?.let { catch({ MemberDateOfBirth(LocalDate.parse(it)) }) { raise(RegisterMemberError.InvalidDateOfBirth) } }
    val email = command.email?.let { catch({ MemberEmail(it) }) { raise(RegisterMemberError.InvalidEmail) } }
    val memberSince = command.memberSince?.let { catch({ MemberSince(LocalDate.parse(it)) }) { raise(RegisterMemberError.InvalidMemberSince) } }

    val member = Member(
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

    save(member)
}

context(Raise<RegisterMemberError>)
private fun RegisterMemberCommand.toMemberType(): Membership =
    catch({ Membership.fromString(type, academyGroups, team) ?: raise(RegisterMemberError.InvalidType) }) {
        raise(RegisterMemberError.InvalidType)
    }

data class RegisterMemberCommand(
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

sealed class RegisterMemberError {
    data object InvalidUUID : RegisterMemberError()
    data object InvalidName : RegisterMemberError()
    data object InvalidSurname : RegisterMemberError()
    data object InvalidPhoneNumbers : RegisterMemberError()
    data object InvalidType : RegisterMemberError()
    data object InvalidIDNumber : RegisterMemberError()
    data object InvalidAddress : RegisterMemberError()
    data object InvalidCity : RegisterMemberError()
    data object InvalidPostalCode : RegisterMemberError()
    data object InvalidDateOfBirth : RegisterMemberError()
    data object InvalidEmail : RegisterMemberError()
    data object InvalidMemberSince : RegisterMemberError()
}
