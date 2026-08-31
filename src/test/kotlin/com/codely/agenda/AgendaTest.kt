package com.codely.agenda

import com.codely.agenda.domain.AvailableHour
import com.codely.agenda.domain.HourType.MEMBERS_TIME
import com.codely.agenda.domain.Player
import java.util.UUID
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class AgendaTest {

    @Test
    fun `should filter out invalid available hours for tuesday on construction`() {
        // Given
        val validHourOne = AvailableHour(id = UUID.randomUUID(), from = 16, to = 17, type = MEMBERS_TIME, registeredPlayers = listOf(Player("R. Nadal")))
        val validHourTwo = AvailableHour(id = UUID.randomUUID(), from = 17, to = 18, type = MEMBERS_TIME, registeredPlayers = listOf(Player("C. Alcaraz")))
        val invalidHour = AvailableHour(id = UUID.randomUUID(), from = 18, to = 19, type = MEMBERS_TIME, registeredPlayers = listOf(Player("N. Djokovic")))

        // When
        val agenda = AgendaMother.random(day = DayMother.tuesday(), availableHours = listOf(validHourOne, validHourTwo, invalidHour))

        // Then
        assertEquals(listOf(validHourOne, validHourTwo), agenda.availableHours)
    }

    @Test
    fun `should preserve valid persisted data when filtering available hours`() {
        // Given
        val validHourWithPlayers = AvailableHour(
            id = UUID.randomUUID(),
            from = 16,
            to = 17,
            type = MEMBERS_TIME,
            registeredPlayers = listOf(Player("Rafa"), Player("Carlos"))
        )

        // When
        val agenda = AgendaMother.random(day = DayMother.tuesday(), availableHours = listOf(validHourWithPlayers))

        // Then
        assertEquals(validHourWithPlayers, agenda.availableHours.single())
    }
}
