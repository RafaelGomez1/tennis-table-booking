package com.codely.departures.primaryadapter.rest

import com.codely.departures.fakes.FakeDepartureRepository
import com.codely.members.MemberMother
import com.codely.members.domain.MemberEmail
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus.OK
import java.time.LocalDate
import com.codely.departures.domain.Departure
import com.codely.departures.domain.DepartureDate

@OptIn(ExperimentalCoroutinesApi::class)
class GetAllDeparturesControllerTest {

    private val repository = FakeDepartureRepository()
    private val controller = GetAllDeparturesController(repository)

    @BeforeEach
    fun setUp() {
        repository.resetFake()
    }

    @Test
    fun `should return all departures`() = runTest {
        val member1 = MemberMother.casual()
        val member2 = MemberMother.coach()
        val departureDate1 = DepartureDate(LocalDate.of(2025, 9, 1))
        val departureDate2 = DepartureDate(LocalDate.of(2025, 10, 1))

        val departure1 = Departure.fromMember(member1, departureDate1)
        val departure2 = Departure.fromMember(member2, departureDate2)

        repository.save(departure1)
        repository.save(departure2)

        val result = controller.getAllDepartures()

        assertEquals(OK, result.statusCode)
        val body = result.body as List<*>
        assertEquals(2, body.size)
    }

    @Test
    fun `should return empty list when no departures`() = runTest {
        val result = controller.getAllDepartures()

        assertEquals(OK, result.statusCode)
        val body = result.body as List<*>
        assertEquals(0, body.size)
    }

    @Test
    fun `departure response should contain member and departure information`() = runTest {
        val member = MemberMother.random(email = MemberEmail("john.doe@example.com"))
        val departureDate = DepartureDate(LocalDate.of(2025, 9, 1))
        val departure = Departure.fromMember(member, departureDate)

        repository.save(departure)

        val result = controller.getAllDepartures()

        assertEquals(OK, result.statusCode)
        val body = result.body as List<*>
        val dto = body[0] as DepartureResponseDTO

        assertEquals(member.id.value.toString(), dto.memberId)
        assertEquals(member.name.value, dto.name)
        assertEquals(member.surname.value, dto.surname)
        assertEquals("2025-09-01", dto.departureDate)
        assertEquals(member.email?.value, dto.email)
    }
}
