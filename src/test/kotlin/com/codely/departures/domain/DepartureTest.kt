package com.codely.departures.domain

import com.codely.members.MemberMother
import com.codely.members.domain.MemberEmail
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import org.junit.jupiter.api.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class DepartureTest {

    @Test
    fun `should create departure from member`() = runTest {
        val member = MemberMother.casual()
        val departureDate = DepartureDate(LocalDate.of(2025, 9, 1))

        val departure = Departure.fromMember(member, departureDate)

        assertEquals(member.id, departure.memberId)
        assertEquals(member.name, departure.name)
        assertEquals(member.surname, departure.surname)
        assertEquals(member.phoneNumbers, departure.phoneNumbers)
        assertEquals(member.type.toName(), departure.type.toName())
        assertEquals(departureDate, departure.departureDate)
        assertNotNull(departure.id)
    }

    @Test
    fun `departure should preserve all member information`() = runTest {
        val member = MemberMother.random(email = MemberEmail("john.doe@example.com"))
        val departureDate = DepartureDate(LocalDate.of(2025, 9, 1))

        val departure = Departure.fromMember(member, departureDate)

        assertEquals(member.email, departure.email)
        assertEquals(member.address, departure.address)
        assertEquals(member.city, departure.city)
        assertEquals(member.postalCode, departure.postalCode)
        assertEquals(member.dateOfBirth, departure.dateOfBirth)
        assertEquals(member.memberSince, departure.memberSince)
    }
}
