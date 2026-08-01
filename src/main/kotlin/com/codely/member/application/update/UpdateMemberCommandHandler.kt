package com.codely.member.application.update

import arrow.core.raise.Raise
import arrow.core.raise.catch
import com.codely.member.domain.ContactPhoneNumber
import com.codely.member.domain.ContactPhoneNumbers
import com.codely.member.domain.MemberId
import com.codely.member.domain.MemberName
import com.codely.member.domain.MemberRepository
import com.codely.member.domain.MemberSurname
import com.codely.member.domain.MemberType
import java.util.UUID

context(MemberRepository, Raise<UpdateMemberError>)
suspend fun handle(command: UpdateMemberCommand) {
    val id = catch({ MemberId(UUID.fromString(command.id)) }) { raise(UpdateMemberError.InvalidUUID) }
    val name = catch({ MemberName(command.name) }) { raise(UpdateMemberError.InvalidName) }
    val surname = catch({ MemberSurname(command.surname) }) { raise(UpdateMemberError.InvalidSurname) }
    val phoneNumbers = catch({
        ContactPhoneNumbers(command.phoneNumbers.map { ContactPhoneNumber(it) })
    }) { raise(UpdateMemberError.InvalidPhoneNumbers) }
    val type = catch({ MemberType.fromString(command.type, command.academyGroup, command.team) })
    { raise(UpdateMemberError.InvalidType) }

    updateMember(id = id, name = name, surname = surname, phoneNumbers = phoneNumbers, type = type)
}

data class UpdateMemberCommand(
    val id: String,
    val name: String,
    val surname: String,
    val phoneNumbers: List<String>,
    val type: String,
    val academyGroup: String? = null,
    val team: String? = null
)

sealed class UpdateMemberError {
    data object InvalidUUID : UpdateMemberError()
    data object MemberNotFound : UpdateMemberError()
    data object InvalidName : UpdateMemberError()
    data object InvalidSurname : UpdateMemberError()
    data object InvalidPhoneNumbers : UpdateMemberError()
    data object InvalidType : UpdateMemberError()
}
