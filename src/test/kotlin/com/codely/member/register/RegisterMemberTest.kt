package com.codely.member.register

import com.codely.member.MemberMother
import com.codely.member.fakes.FakeMemberRepository
import com.codely.member.primaryadapter.rest.error.MemberServerErrors.INVALID_IDENTIFIERS
import com.codely.member.primaryadapter.rest.error.MemberServerErrors.INVALID_NAME
import com.codely.member.primaryadapter.rest.error.MemberServerErrors.INVALID_PHONE_NUMBERS
import com.codely.member.primaryadapter.rest.error.MemberServerErrors.INVALID_TYPE
import com.codely.member.primaryadapter.rest.register.RegisterMemberController
import com.codely.member.primaryadapter.rest.register.RegisterMemberDTO
import com.codely.shared.error.ServerError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.CREATED

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterMemberTest {

    private val repository = FakeMemberRepository()
    private val controller = RegisterMemberController(repository)

    @BeforeEach
    fun setUp() {
        repository.resetFake()
    }

    @Test
    fun `should register a casual member`() = runTest {
        val member = MemberMother.casual()

        val result = controller.register(
            member.id.value.toString(),
            RegisterMemberDTO(
                name = member.name.value,
                surname = member.surname.value,
                phoneNumbers = member.phoneNumbers.values.map { it.value },
                type = "CASUAL"
            )
        )

        assertEquals(CREATED, result.statusCode)
        repository.assertContains(member)
    }

    @Test
    fun `should register an academy beginner member`() = runTest {
        val member = MemberMother.academyBeginner()
        val type = member.type as com.codely.member.domain.MemberType.AcademyBeginner

        val result = controller.register(
            member.id.value.toString(),
            RegisterMemberDTO(
                name = member.name.value,
                surname = member.surname.value,
                phoneNumbers = member.phoneNumbers.values.map { it.value },
                type = "ACADEMY_BEGINNER",
                academyGroup = type.group.name
            )
        )

        assertEquals(CREATED, result.statusCode)
        repository.assertContains(member)
    }

    @Test
    fun `should register a competition member`() = runTest {
        val member = MemberMother.competition()
        val type = member.type as com.codely.member.domain.MemberType.Competition

        val result = controller.register(
            member.id.value.toString(),
            RegisterMemberDTO(
                name = member.name.value,
                surname = member.surname.value,
                phoneNumbers = member.phoneNumbers.values.map { it.value },
                type = "COMPETITION",
                team = type.team.name
            )
        )

        assertEquals(CREATED, result.statusCode)
        repository.assertContains(member)
    }

    @Test
    fun `should fail if id is not a valid UUID`() = runTest {
        val result = controller.register(
            "not-a-uuid",
            RegisterMemberDTO(
                name = "Carlos",
                surname = "Garcia",
                phoneNumbers = listOf("612345678"),
                type = "CASUAL"
            )
        )

        assertEquals(BAD_REQUEST, result.statusCode)
        assertEquals(ServerError.of(INVALID_IDENTIFIERS), result.body)
    }

    @Test
    fun `should fail if name is blank`() = runTest {
        val member = MemberMother.casual()

        val result = controller.register(
            member.id.value.toString(),
            RegisterMemberDTO(
                name = "",
                surname = "Garcia",
                phoneNumbers = listOf("612345678"),
                type = "CASUAL"
            )
        )

        assertEquals(BAD_REQUEST, result.statusCode)
        assertEquals(ServerError.of(INVALID_NAME), result.body)
    }

    @Test
    fun `should fail if phone numbers are empty`() = runTest {
        val member = MemberMother.casual()

        val result = controller.register(
            member.id.value.toString(),
            RegisterMemberDTO(
                name = "Carlos",
                surname = "Garcia",
                phoneNumbers = emptyList(),
                type = "CASUAL"
            )
        )

        assertEquals(BAD_REQUEST, result.statusCode)
        assertEquals(ServerError.of(INVALID_PHONE_NUMBERS), result.body)
    }

    @Test
    fun `should fail if type is invalid`() = runTest {
        val member = MemberMother.casual()

        val result = controller.register(
            member.id.value.toString(),
            RegisterMemberDTO(
                name = "Carlos",
                surname = "Garcia",
                phoneNumbers = listOf("612345678"),
                type = "UNKNOWN"
            )
        )

        assertEquals(BAD_REQUEST, result.statusCode)
        assertEquals(ServerError.of(INVALID_TYPE), result.body)
    }
}
