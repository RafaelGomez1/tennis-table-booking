package com.codely.members.delete

import com.codely.departures.fakes.FakeDepartureRepository
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
import kotlin.test.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.NO_CONTENT
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class DeleteMemberTest {

    private val memberRepository = FakeMemberRepository()
    private val departureRepository = FakeDepartureRepository()
    private val controller = DeleteMemberController(memberRepository, departureRepository)

    @BeforeEach
    fun setUp() {
        memberRepository.resetFake()
        departureRepository.resetFake()
    }

    @Test
    fun `should delete an existing member`() = runTest {
        val member = MemberMother.casual()
        memberRepository.save(member)

        val result = controller.delete(member.id.value.toString(), null)

        assertEquals(NO_CONTENT, result.statusCode)
        assertFalse(memberRepository.resourceExistsById(member.id))
    }

    @Test
    fun `should create departure when deleting member with departure date`() = runTest {
        val member = MemberMother.casual()
        memberRepository.save(member)
        val departureDate = LocalDate.of(2025, 9, 1).toString()

        val result = controller.delete(member.id.value.toString(), departureDate)

        assertEquals(NO_CONTENT, result.statusCode)
        assertFalse(memberRepository.resourceExistsById(member.id))
        assertEquals(1, departureRepository.findAll().size)
        val departure = departureRepository.findAll().first()
        assertEquals(member.id, departure.memberId)
        assertEquals(departureDate, departure.departureDate.value.toString())
    }

    @Test
    fun `should delete member without departure if no departure date provided`() = runTest {
        val member = MemberMother.casual()
        memberRepository.save(member)

        val result = controller.delete(member.id.value.toString(), null)

        assertEquals(NO_CONTENT, result.statusCode)
        assertFalse(memberRepository.resourceExistsById(member.id))
        assertEquals(0, departureRepository.findAll().size)
    }

    @Test
    fun `should fail if member does not exist`() = runTest {
        val member = MemberMother.casual()

        val result = controller.delete(member.id.value.toString(), null)

        assertEquals(NOT_FOUND, result.statusCode)
        assertEquals(ServerError.of(MEMBER_NOT_FOUND), result.body)
    }

    @Test
    fun `should fail if id is not a valid UUID`() = runTest {
        val result = controller.delete("not-a-uuid", null)

        assertEquals(BAD_REQUEST, result.statusCode)
        assertEquals(ServerError.of(INVALID_IDENTIFIERS), result.body)
    }

    @Test
    fun `should fail without deleting member when departure creation fails`() = runTest {
        val member = MemberMother.casual()
        memberRepository.save(member)

        val result = controller.delete(member.id.value.toString(), "invalid-date")

        assertEquals(INTERNAL_SERVER_ERROR, result.statusCode)
        assertEquals(
            ServerError.of("error" to "Failed to create departure record"),
            result.body
        )
        assertTrue(memberRepository.resourceExistsById(member.id))
        assertEquals(0, departureRepository.findAll().size)
    }
}
