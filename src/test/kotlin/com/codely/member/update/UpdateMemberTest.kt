package com.codely.member.update

import com.codely.member.MemberMother
import com.codely.member.domain.MemberType
import com.codely.member.fakes.FakeMemberRepository
import com.codely.member.primaryadapter.rest.error.MemberServerErrors.INVALID_NAME
import com.codely.member.primaryadapter.rest.error.MemberServerErrors.MEMBER_NOT_FOUND
import com.codely.member.primaryadapter.rest.update.UpdateMemberController
import com.codely.member.primaryadapter.rest.update.UpdateMemberDTO
import com.codely.shared.error.ServerError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK

@OptIn(ExperimentalCoroutinesApi::class)
class UpdateMemberTest {

    private val repository = FakeMemberRepository()
    private val controller = UpdateMemberController(repository)

    @BeforeEach
    fun setUp() {
        repository.resetFake()
    }

    @Test
    fun `should update an existing member`() = runTest {
        val member = MemberMother.casual()
        repository.save(member)

        val result = controller.update(
            member.id.value.toString(),
            UpdateMemberDTO(
                name = "NewName",
                surname = "NewSurname",
                phoneNumbers = listOf("699999999"),
                type = "COMPETITION",
                team = "TWO_A"
            )
        )

        assertEquals(OK, result.statusCode)
        val updated = repository.find(com.codely.member.domain.MemberFindByCriteria.ById(member.id))!!
        assertEquals("NewName", updated.name.value)
        assertEquals("NewSurname", updated.surname.value)
        assertEquals(MemberType.Competition(com.codely.member.domain.Team.TWO_A), updated.type)
    }

    @Test
    fun `should fail if member does not exist`() = runTest {
        val member = MemberMother.casual()

        val result = controller.update(
            member.id.value.toString(),
            UpdateMemberDTO(
                name = "Name",
                surname = "Surname",
                phoneNumbers = listOf("612345678"),
                type = "CASUAL"
            )
        )

        assertEquals(NOT_FOUND, result.statusCode)
        assertEquals(ServerError.of(MEMBER_NOT_FOUND), result.body)
    }

    @Test
    fun `should fail if name is blank`() = runTest {
        val member = MemberMother.casual()
        repository.save(member)

        val result = controller.update(
            member.id.value.toString(),
            UpdateMemberDTO(
                name = "",
                surname = "Surname",
                phoneNumbers = listOf("612345678"),
                type = "CASUAL"
            )
        )

        assertEquals(BAD_REQUEST, result.statusCode)
        assertEquals(ServerError.of(INVALID_NAME), result.body)
    }
}
