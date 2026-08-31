package com.codely.stats.application.calculate

import com.codely.agenda.domain.Agenda
import com.codely.stats.domain.AgendaStats
import com.codely.stats.domain.OccupancyLevel
import com.codely.stats.domain.SlotStats
import java.time.DayOfWeek
import java.time.Month

fun calculateStats(month: Month, year: Int, agendas: List<Agenda>): AgendaStats {
    val slots = agendas
        .flatMap { agenda ->
            agenda.availableHours.map { hour ->
                Triple(DayOfWeek.valueOf(agenda.day.dayOfWeek.name), hour.from, hour)
            }
        }
        .groupBy { (dayOfWeek, hourFrom, _) -> dayOfWeek to hourFrom }
        .map { (key, entries) ->
            val (dayOfWeek, hour) = key
            val totalSessions = entries.size
            val totalCapacity = entries.sumOf { (_, _, h) -> h.capacity.value }
            val totalBookings = entries.sumOf { (_, _, h) -> h.registeredPlayers.size }
            val occupancyRate = if (totalCapacity > 0) totalBookings.toDouble() / totalCapacity else 0.0

            SlotStats(
                dayOfWeek = dayOfWeek,
                hour = hour,
                totalSessions = totalSessions,
                totalCapacity = totalCapacity,
                totalBookings = totalBookings,
                occupancyRate = occupancyRate,
                classification = OccupancyLevel.from(occupancyRate)
            )
        }
        .sortedWith(compareBy({ it.dayOfWeek }, { it.hour }))

    return AgendaStats.create(month, year, slots)
}
