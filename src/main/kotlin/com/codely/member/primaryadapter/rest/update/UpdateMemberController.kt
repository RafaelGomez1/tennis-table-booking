package com.codely.member.primaryadapter.rest.update

import arrow.core.raise.fold
import com.codely.member.application.update.UpdateMemberCommand
import com.codely.member.application.update.UpdateMemberError
import com.codely.member.application.update.UpdateMemberError.InvalidName
import com.codely.member.application.update.UpdateMemberError.InvalidPhoneNumbers
import com.codely.member.application.update.UpdateMemberError.InvalidSurname
import com.codely.member.application.update.UpdateMemberError.InvalidType
import com.codely.member.application.update.UpdateMemberError.InvalidUUID
import com.codely.member.application.update.UpdateMemberError.MemberNotFound
import com.codely.member.application.update.UpdateMemberError.InvalidIDNumber
import com.codely.member.application.update.UpdateMemberError.InvalidAddress
import com.codely.member.application.update.UpdateMemberError.InvalidCity
import com.codely.member.application.update.UpdateMemberError.InvalidPostalCode
import com.codely.member.application.update.UpdateMemberError.InvalidDateOfBirth
import com.codely.member.application.update.UpdateMemberError.InvalidEmail
import com.codely.member.application.update.UpdateMemberError.InvalidMemberSince
import com.codely.member.application.update.handle
import com.codely.member.domain.MemberRepository
import com.codely.member.primaryadapter.rest.error.MemberServerErrors.INVALID_IDENTIFIERS
import com.codely.member.primaryadapter.rest.error.MemberServerErrors.INVALID_NAME
import com.codely.member.primaryadapter.rest.error.MemberServerErrors.INVALID_PHONE_NUMBERS
import com.codely.member.primaryadapter.rest.error.MemberServerErrors.INVALID_SURNAME
import com.codely.member.primaryadapter.rest.error.MemberServerErrors.INVALID_TYPE
import com.codely.member.primaryadapter.rest.error.MemberServerErrors.MEMBER_NOT_FOUND
import com.codely.member.primaryadapter.rest.error.MemberServerErrors.INVALID_MEMBER_DATA
import com.codely.shared.cors.BaseController
import com.codely.shared.response.Response
import com.codely.shared.response.withBody
import kotlinx.coroutines.coroutineScope
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UpdateMemberController(
    private val repository: MemberRepository
) : BaseController() {

    @PutMapping("/api/members/{memberId}")
    suspend fun update(@PathVariable memberId: String, @RequestBody body: UpdateMemberDTO): Response<*> =
        coroutineScope {
            with(repository) {
                fold(
                    block = {
                        handle(
                            UpdateMemberCommand(
                                id = memberId,
                                name = body.name,
                                surname = body.surname,
                                phoneNumbers = body.phoneNumbers,
                                type = body.type,
                                academyGroups = body.academyGroups,
                                team = body.team,
                                idNumber = body.idNumber,
                                address = body.address,
                                city = body.city,
                                postalCode = body.postalCode,
                                dateOfBirth = body.dateOfBirth,
                                email = body.email,
                                memberSince = body.memberSince
                            )
                        )
                    },
                    recover = { error -> error.toServerError() },
                    transform = { Response.status(OK).body(null) }
                )
            }
        }

    private fun UpdateMemberError.toServerError(): Response<*> =
        when (this) {
            is InvalidUUID -> Response.status(BAD_REQUEST).withBody(INVALID_IDENTIFIERS)
            is MemberNotFound -> Response.status(NOT_FOUND).withBody(MEMBER_NOT_FOUND)
            is InvalidName -> Response.status(BAD_REQUEST).withBody(INVALID_NAME)
            is InvalidSurname -> Response.status(BAD_REQUEST).withBody(INVALID_SURNAME)
            is InvalidPhoneNumbers -> Response.status(BAD_REQUEST).withBody(INVALID_PHONE_NUMBERS)
            is InvalidType -> Response.status(BAD_REQUEST).withBody(INVALID_TYPE)
            is InvalidIDNumber -> Response.status(BAD_REQUEST).withBody(INVALID_MEMBER_DATA)
            is InvalidAddress -> Response.status(BAD_REQUEST).withBody(INVALID_MEMBER_DATA)
            is InvalidCity -> Response.status(BAD_REQUEST).withBody(INVALID_MEMBER_DATA)
            is InvalidPostalCode -> Response.status(BAD_REQUEST).withBody(INVALID_MEMBER_DATA)
            is InvalidDateOfBirth -> Response.status(BAD_REQUEST).withBody(INVALID_MEMBER_DATA)
            is InvalidEmail -> Response.status(BAD_REQUEST).withBody(INVALID_MEMBER_DATA)
            is InvalidMemberSince -> Response.status(BAD_REQUEST).withBody(INVALID_MEMBER_DATA)
        }
}
