package com.codely.member.primaryadapter.rest.search

import arrow.core.raise.fold
import com.codely.member.application.search.SearchMembersError
import com.codely.member.application.search.SearchMembersQuery
import com.codely.member.application.search.handle
import com.codely.member.domain.MemberRepository
import com.codely.member.primaryadapter.rest.MemberResponseDTO
import com.codely.member.primaryadapter.rest.error.MemberServerErrors.INVALID_TYPE
import com.codely.shared.cors.BaseController
import com.codely.shared.response.Response
import com.codely.shared.response.withBody
import kotlinx.coroutines.coroutineScope
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SearchMembersController(private val repository: MemberRepository) : BaseController() {

    @GetMapping("/api/members")
    suspend fun search(
        @RequestParam(required = false) type: String?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): Response<*> = coroutineScope {
        with(repository) {
            fold(
                block = { handle(SearchMembersQuery(type = type, page = page, size = size)) },
                recover = { error -> error.toServerError() },
                transform = { result -> Response.status(OK).body(MemberResponseDTO.fromDomain(result)) }
            )
        }
    }

    private fun SearchMembersError.toServerError(): Response<*> =
        when (this) {
            is SearchMembersError.InvalidType -> Response.status(BAD_REQUEST).withBody(INVALID_TYPE)
        }
}
