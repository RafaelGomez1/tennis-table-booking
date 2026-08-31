package com.codely.stats.primaryadapter.rest.search

import com.codely.stats.domain.AgendaStats
import com.codely.stats.domain.OccupancyLevel
import com.codely.stats.domain.SlotStats
import java.time.DayOfWeek
import java.time.Month

data class StatsResponseDTO(
    val month: String,
    val year: Int,
    val slots: List<SlotStatsDTO>
) {
    companion object {
        fun from(stats: AgendaStats) = StatsResponseDTO(
            month = stats.month.name,
            year = stats.year,
            slots = stats.slots.map { SlotStatsDTO.from(it) }
        )
    }
}

data class SlotStatsDTO(
    val dayOfWeek: String,
    val hour: Int,
    val totalSessions: Int,
    val totalCapacity: Int,
    val totalBookings: Int,
    val occupancyRate: Double,
    val classification: String
) {
    companion object {
        fun from(slot: SlotStats) = SlotStatsDTO(
            dayOfWeek = slot.dayOfWeek.name,
            hour = slot.hour,
            totalSessions = slot.totalSessions,
            totalCapacity = slot.totalCapacity,
            totalBookings = slot.totalBookings,
            occupancyRate = slot.occupancyRate,
            classification = slot.classification.name
        )
    }
}
