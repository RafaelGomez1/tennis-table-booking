package com.codely.stats.secondaryadapter.database.document

import com.codely.stats.domain.AgendaStats
import com.codely.stats.domain.OccupancyLevel
import com.codely.stats.domain.SlotStats
import java.time.DayOfWeek
import java.time.Month
import java.util.UUID
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "AgendaStats")
@CompoundIndex(def = "{'month': 1, 'year': 1}", unique = true)
class StatsDocument(
    @Id val id: String,
    val month: String,
    val year: Int,
    val slots: List<SlotStatsDocument>
) {
    fun toAgendaStats() = AgendaStats(
        id = UUID.fromString(id),
        month = Month.valueOf(month),
        year = year,
        slots = slots.map { it.toSlotStats() }
    )
}

data class SlotStatsDocument(
    val dayOfWeek: String,
    val hour: Int,
    val totalSessions: Int,
    val totalCapacity: Int,
    val totalBookings: Int,
    val occupancyRate: Double,
    val classification: String
) {
    fun toSlotStats() = SlotStats(
        dayOfWeek = DayOfWeek.valueOf(dayOfWeek),
        hour = hour,
        totalSessions = totalSessions,
        totalCapacity = totalCapacity,
        totalBookings = totalBookings,
        occupancyRate = occupancyRate,
        classification = OccupancyLevel.valueOf(classification)
    )
}

internal fun AgendaStats.toDocument() = StatsDocument(
    id = id.toString(),
    month = month.name,
    year = year,
    slots = slots.map { it.toDocument() }
)

internal fun SlotStats.toDocument() = SlotStatsDocument(
    dayOfWeek = dayOfWeek.name,
    hour = hour,
    totalSessions = totalSessions,
    totalCapacity = totalCapacity,
    totalBookings = totalBookings,
    occupancyRate = occupancyRate,
    classification = classification.name
)
