package com.codely.stats.application

import com.codely.agenda.AgendaMother
import com.codely.agenda.DayMother
import com.codely.agenda.domain.AvailableHour
import com.codely.agenda.domain.HourType
import com.codely.agenda.domain.MaxCapacity
import com.codely.agenda.domain.Player
import com.codely.stats.application.calculate.calculateStats
import com.codely.stats.domain.OccupancyLevel
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import java.time.DayOfWeek
import java.time.Month

class AgendaStatsCalculatorTest : FunSpec({

    test("should return empty slots when no agendas exist") {
        val stats = calculateStats(Month.JANUARY, 2024, emptyList())

        stats.month shouldBe Month.JANUARY
        stats.year shouldBe 2024
        stats.slots shouldHaveSize 0
    }

    test("should calculate occupancy rate correctly for a single fully booked slot") {
        val agenda = AgendaMother.random(
            day = DayMother.monday(),
            availableHours = listOf(
                AvailableHour(
                    from = 16, to = 17,
                    capacity = MaxCapacity(4),
                    type = HourType.MEMBERS_TIME,
                    registeredPlayers = listOf(Player("A"), Player("B"), Player("C"), Player("D"))
                )
            )
        )

        val stats = calculateStats(Month.JANUARY, 2024, listOf(agenda))

        stats.slots shouldHaveSize 1
        val slot = stats.slots.first()
        slot.dayOfWeek shouldBe DayOfWeek.MONDAY
        slot.hour shouldBe 16
        slot.totalSessions shouldBe 1
        slot.totalCapacity shouldBe 4
        slot.totalBookings shouldBe 4
        slot.occupancyRate shouldBe 1.0
        slot.classification shouldBe OccupancyLevel.ALWAYS_FULL
    }

    test("should aggregate multiple sessions for the same day and hour") {
        val hour1 = AvailableHour(
            from = 16, to = 17,
            capacity = MaxCapacity(8),
            type = HourType.MEMBERS_TIME,
            registeredPlayers = listOf(Player("A"), Player("B"))
        )
        val hour2 = AvailableHour(
            from = 16, to = 17,
            capacity = MaxCapacity(8),
            type = HourType.MEMBERS_TIME,
            registeredPlayers = listOf(Player("C"), Player("D"), Player("E"), Player("F"))
        )
        val agenda1 = AgendaMother.random(day = DayMother.monday(), availableHours = listOf(hour1))
        val agenda2 = AgendaMother.random(day = DayMother.monday(), availableHours = listOf(hour2))

        val stats = calculateStats(Month.JANUARY, 2024, listOf(agenda1, agenda2))

        stats.slots shouldHaveSize 1
        val slot = stats.slots.first()
        slot.totalSessions shouldBe 2
        slot.totalCapacity shouldBe 16
        slot.totalBookings shouldBe 6
        slot.occupancyRate shouldBe 6.0 / 16.0
        slot.classification shouldBe OccupancyLevel.MODERATE
    }

    test("should produce separate slots for different days and hours") {
        val mondayAgenda = AgendaMother.monday(day = DayMother.monday())
        val tuesdayAgenda = AgendaMother.tuesday(day = DayMother.tuesday())

        val stats = calculateStats(Month.JANUARY, 2024, listOf(mondayAgenda, tuesdayAgenda))

        stats.slots shouldHaveSize 4 // 2 hours per day * 2 days
    }

    test("should classify an empty slot as ALWAYS_EMPTY") {
        val agenda = AgendaMother.random(
            day = DayMother.monday(),
            availableHours = listOf(
                AvailableHour(
                    from = 16, to = 17,
                    capacity = MaxCapacity(8),
                    type = HourType.MEMBERS_TIME,
                    registeredPlayers = emptyList()
                )
            )
        )

        val stats = calculateStats(Month.JANUARY, 2024, listOf(agenda))

        stats.slots.first().classification shouldBe OccupancyLevel.ALWAYS_EMPTY
    }
})
