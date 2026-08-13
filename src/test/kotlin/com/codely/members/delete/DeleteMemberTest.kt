package com.codely.members.delete

import com.codely.members.MemberMother
import com.codely.members.fakes.FakeMemberRepository
import com.codely.members.primaryadapter.rest.error.MemberServerErrors.INVALID_IDENTIFIERS
import com.codely.members.primaryadapter.rest.error.MemberServerErrors.MEMBER_NOT_FOUND
import com.codely.members.primaryadapter.rest.delete.DeleteMemberController
import com.codely.shared.error.ServerError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.NO_CONTENT

@OptIn(ExperimentalCoroutinesApi::class)
class DeleteMemberTest {

    private val repository = FakeMemberRepository()
    private val controller = DeleteMemberController(repository)

    @BeforeEach
    fun setUp() {
        repository.resetFake()
    }

    @Test
    fun `should delete an existing member`() = runTest {
        val member = MemberMother.casual()
        repository.save(member)

        val result = controller.delete(member.id.value.toString())

        assertEquals(NO_CONTENT, result.statusCode)
        assertFalse(repository.resourceExistsById(member.id))
    }

    @Test
    fun `should fail if member does not exist`() = runTest {
        val member = MemberMother.casual()

        val result = controller.delete(member.id.value.toString())

        assertEquals(NOT_FOUND, result.statusCode)
        assertEquals(ServerError.of(MEMBER_NOT_FOUND), result.body)
    }

    @Test
    fun `should fail if id is not a valid UUID`() = runTest {
        val result = controller.delete("not-a-uuid")

        assertEquals(BAD_REQUEST, result.statusCode)
        assertEquals(ServerError.of(INVALID_IDENTIFIERS), result.body)
    }
}
