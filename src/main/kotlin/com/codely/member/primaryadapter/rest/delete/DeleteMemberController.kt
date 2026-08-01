package com.codely.member.primaryadapter.rest.delete

import arrow.core.raise.fold
import com.codely.member.application.delete.DeleteMemberCommand
import com.codely.member.application.delete.DeleteMemberError
import com.codely.member.application.delete.DeleteMemberError.InvalidUUID
import com.codely.member.application.delete.DeleteMemberError.MemberNotFound
import com.codely.member.application.delete.handle
import com.codely.member.domain.MemberRepository
import com.codely.member.primaryadapter.rest.error.MemberServerErrors.INVALID_IDENTIFIERS
import com.codely.member.primaryadapter.rest.error.MemberServerErrors.MEMBER_NOT_FOUND
import com.codely.shared.cors.BaseController
import com.codely.shared.response.Response
import com.codely.shared.response.withBody
import kotlinx.coroutines.coroutineScope
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class DeleteMemberController(
    private val repository: MemberRepository
) : BaseController() {

    @DeleteMapping("/api/members/{memberId}")
    suspend fun delete(@PathVariable memberId: String): Response<*> =
        coroutineScope {
            with(repository) {
                fold(
                    block = { handle(DeleteMemberCommand(memberId)) },
                    recover = { error -> error.toServerError() },
                    transform = { Response.status(NO_CONTENT).body(null) }
                )
            }
        }

    private fun DeleteMemberError.toServerError(): Response<*> =
        when (this) {
            is InvalidUUID -> Response.status(BAD_REQUEST).withBody(INVALID_IDENTIFIERS)
            is MemberNotFound -> Response.status(NOT_FOUND).withBody(MEMBER_NOT_FOUND)
        }
}
