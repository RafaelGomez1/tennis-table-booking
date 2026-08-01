package com.codely.member.application.register

import arrow.core.raise.Raise
import arrow.core.raise.catch
import com.codely.member.domain.ContactPhoneNumber
import com.codely.member.domain.ContactPhoneNumbers
import com.codely.member.domain.Member
import com.codely.member.domain.MemberId
import com.codely.member.domain.MemberName
import com.codely.member.domain.MemberRepository
import com.codely.member.domain.MemberSurname
import com.codely.member.domain.MemberType
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

    val member = Member(id = id, name = name, surname = surname, phoneNumbers = phoneNumbers, type = type)

    save(member)
}

context(Raise<RegisterMemberError>)
private fun RegisterMemberCommand.toMemberType(): MemberType =
    catch({ MemberType.fromString(type, academyGroup, team) ?: raise(RegisterMemberError.InvalidType) }) {
        raise(RegisterMemberError.InvalidType)
    }

data class RegisterMemberCommand(
    val id: String,
    val name: String,
    val surname: String,
    val phoneNumbers: List<String>,
    val type: String,
    val academyGroup: String? = null,
    val team: String? = null
)

sealed class RegisterMemberError {
    data object InvalidUUID : RegisterMemberError()
    data object InvalidName : RegisterMemberError()
    data object InvalidSurname : RegisterMemberError()
    data object InvalidPhoneNumbers : RegisterMemberError()
    data object InvalidType : RegisterMemberError()
}
