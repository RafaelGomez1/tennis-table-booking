package com.codely.members.application.delete

import arrow.core.raise.fold
import com.codely.departures.fakes.FakeDepartureRepository
import com.codely.members.MemberMother
import com.codely.members.fakes.FakeMemberRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class DeleteMemberCommandHandlerTest {

    private val memberRepository = FakeMemberRepository()
    private val departureRepository = FakeDepartureRepository()

    @BeforeEach
    fun setUp() {
        memberRepository.resetFake()
        departureRepository.resetFake()
    }

    @Test
    fun `should create a departure before deleting the member`() = runTest {
        val member = MemberMother.casual()
        val departureDate = LocalDate.of(2026, 9, 5).toString()
        memberRepository.save(member)

        val result = with(memberRepository) {
            with(departureRepository) {
                fold(
                    block = {
                        handle(DeleteMemberCommand(member.id.value.toString(), departureDate))
                        "ok"
                    },
                    recover = { error -> error::class.simpleName ?: "error" },
                    transform = { it }
                )
            }
        }

        assertEquals("ok", result)
        assertFalse(memberRepository.resourceExistsById(member.id))
        val departure = departureRepository.findAll().single()
        assertEquals(member.id, departure.memberId)
        assertEquals(departureDate, departure.departureDate.value.toString())
    }
}
