package com.codely.stats.domain

import com.codely.agenda.domain.Year
import java.time.DayOfWeek
import java.time.Month
import java.util.UUID

data class AgendaStats(
    val id: UUID,
    val month: Month,
    val year: Year,
    val slots: List<SlotStats>
) {
    companion object {
        fun create(month: Month, year: Year, slots: List<SlotStats>) =
            AgendaStats(UUID.randomUUID(), month, year, slots)
    }
}

data class SlotStats(
    val dayOfWeek: DayOfWeek,
    val hour: Int,
    val totalSessions: Int,
    val totalCapacity: Int,
    val totalBookings: Int,
    val occupancyRate: Double,
    val classification: OccupancyLevel
)

enum class OccupancyLevel {
    ALWAYS_FULL,
    USUALLY_FULL,
    MODERATE,
    USUALLY_EMPTY,
    ALWAYS_EMPTY;

    companion object {
        fun from(occupancyRate: Double): OccupancyLevel =
            when {
                occupancyRate >= 1.0 -> ALWAYS_FULL
                occupancyRate >= 0.75 -> USUALLY_FULL
                occupancyRate >= 0.25 -> MODERATE
                occupancyRate > 0.0 -> USUALLY_EMPTY
                else -> ALWAYS_EMPTY
            }
    }
}
