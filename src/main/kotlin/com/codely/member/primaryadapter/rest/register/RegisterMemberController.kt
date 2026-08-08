package com.codely.member.primaryadapter.rest.register

import arrow.core.raise.fold
import com.codely.member.application.register.RegisterMemberCommand
import com.codely.member.application.register.RegisterMemberError
import com.codely.member.application.register.RegisterMemberError.InvalidName
import com.codely.member.application.register.RegisterMemberError.InvalidPhoneNumbers
import com.codely.member.application.register.RegisterMemberError.InvalidSurname
import com.codely.member.application.register.RegisterMemberError.InvalidType
import com.codely.member.application.register.RegisterMemberError.InvalidUUID
import com.codely.member.application.register.RegisterMemberError.InvalidIDNumber
import com.codely.member.application.register.RegisterMemberError.InvalidAddress
import com.codely.member.application.register.RegisterMemberError.InvalidCity
import com.codely.member.application.register.RegisterMemberError.InvalidPostalCode
import com.codely.member.application.register.RegisterMemberError.InvalidDateOfBirth
import com.codely.member.application.register.RegisterMemberError.InvalidEmail
import com.codely.member.application.register.RegisterMemberError.InvalidMemberSince
import com.codely.member.application.register.handle
import com.codely.member.domain.MemberRepository
import com.codely.member.primaryadapter.rest.error.MemberServerErrors.INVALID_IDENTIFIERS
import com.codely.member.primaryadapter.rest.error.MemberServerErrors.INVALID_NAME
import com.codely.member.primaryadapter.rest.error.MemberServerErrors.INVALID_PHONE_NUMBERS
import com.codely.member.primaryadapter.rest.error.MemberServerErrors.INVALID_SURNAME
import com.codely.member.primaryadapter.rest.error.MemberServerErrors.INVALID_TYPE
import com.codely.member.primaryadapter.rest.error.MemberServerErrors.INVALID_MEMBER_DATA
import com.codely.shared.cors.BaseController
import com.codely.shared.response.Response
import com.codely.shared.response.withBody
import kotlinx.coroutines.coroutineScope
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class RegisterMemberController(
    private val repository: MemberRepository
) : BaseController() {

    @PostMapping("/api/members/{memberId}")
    suspend fun register(@PathVariable memberId: String, @RequestBody body: RegisterMemberDTO): Response<*> =
        coroutineScope {
            with(repository) {
                fold(
                    block = {
                        handle(
                            RegisterMemberCommand(
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
                    transform = { Response.status(CREATED).body(null) }
                )
            }
        }

    private fun RegisterMemberError.toServerError(): Response<*> =
        when (this) {
            is InvalidUUID -> Response.status(BAD_REQUEST).withBody(INVALID_IDENTIFIERS)
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
