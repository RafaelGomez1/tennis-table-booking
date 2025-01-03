package com.codely.agenda.domain

import arrow.core.raise.Raise
import com.codely.agenda.domain.BookAgendaErrorDomain.AvailableHourNotFound
import com.codely.agenda.domain.BookAgendaErrorDomain.MaxCapacityReached
import com.codely.agenda.domain.BookAgendaErrorDomain.PlayerAlreadyBooked
import com.codely.agenda.domain.CancelBookingErrorDomain.PlayerNotBooked
import com.codely.agenda.domain.HourType.MEMBERS_TIME
import java.time.LocalDate
import java.time.Month
import java.time.temporal.WeekFields
import java.util.Locale
import java.util.UUID

data class Agenda(
    val id: UUID,
    val day: Day,
    val month: Month,
    val week: Week,
    val year: Year,
    val availableHours: List<AvailableHour> = emptyList()
) {

    companion object {
        fun from(day: Day, localDate: LocalDate) =
            Agenda(
                UUID.randomUUID(),
                day,
                localDate.month,
                localDate.get(WeekFields.of(Locale.FRANCE).weekOfYear()),
                localDate.year,
                AvailableHour.fromDay(day)
            )
    }

    fun disable(): Agenda = copy(availableHours = emptyList())
    fun reenable(): Agenda = copy(availableHours = AvailableHour.fromDay(day))

    context(Raise<BookAgendaErrorDomain>)
    fun bookAvailableHour(availableHourId: UUID, player: Player): Agenda {
        val availableHour = availableHours.find { it.id == availableHourId }

        if (availableHours.any { hour -> player in hour.registeredPlayers }) raise(PlayerAlreadyBooked)

        return availableHour?.let { hour ->
            if (hour.maxCapacityReached()) raise(MaxCapacityReached)

            val updatedHour = hour.addPlayer(player)
            val updatedHours = availableHours.toMutableList()
                .apply {
                    val index = indexOf(hour)
                    set(index, updatedHour)
                }

            copy(availableHours = updatedHours)
        } ?: raise(AvailableHourNotFound)
    }

    context(Raise<CancelBookingErrorDomain>)
    fun cancelBooking(
        availableHourId: UUID,
        playerName: Player
    ): Agenda {
        val availableHour = availableHours.find { it.id == availableHourId }

        return availableHour?.let { hour ->
            if (playerName !in hour.registeredPlayers) raise(PlayerNotBooked)

            val updatedHour = hour.removePlayer(playerName)
            val updatedHours = availableHours.toMutableList()
                .apply {
                    val index = indexOf(hour)
                    set(index, updatedHour)
                }

            copy(availableHours = updatedHours)
        } ?: raise(CancelBookingErrorDomain.AvailableHourNotFound)
    }
}

data class AvailableHour(
    val id: UUID = UUID.randomUUID(),
    val from: Int,
    val to: Int,
    val capacity: MaxCapacity = MaxCapacity(8),
    val type: HourType,
    val registeredPlayers: List<Player>
) {

    fun addPlayer(player: Player): AvailableHour = copy(registeredPlayers = registeredPlayers + player)
    fun removePlayer(player: Player): AvailableHour = copy(registeredPlayers = registeredPlayers - player)

    fun maxCapacityReached() = registeredPlayers.size >= capacity.value

    companion object {
        fun fromDay(day: Day) =
            when (day.dayOfWeek.value) {
                1 -> monday()
                2 -> tuesday()
                3 -> wednesday()
                4 -> thursday()
                5 -> friday()
                6 -> emptyList()
                7 -> emptyList()
                else -> throw IllegalArgumentException("")
            }

        fun monday() = listOf(
            AvailableHour(from = 16, to = 17, registeredPlayers = emptyList(), type = MEMBERS_TIME),
            AvailableHour(from = 17, to = 18, registeredPlayers = emptyList(), type = MEMBERS_TIME)
        )

        fun tuesday() = listOf(
            AvailableHour(from = 16, to = 17, registeredPlayers = emptyList(), type = MEMBERS_TIME),
            AvailableHour(from = 17, to = 18, registeredPlayers = emptyList(), type = MEMBERS_TIME),
            AvailableHour(from = 18, to = 19, registeredPlayers = emptyList(), type = MEMBERS_TIME)
        )

        fun wednesday() = listOf(
            AvailableHour(from = 16, to = 17, registeredPlayers = emptyList(), type = MEMBERS_TIME),
            AvailableHour(from = 17, to = 18, registeredPlayers = emptyList(), type = MEMBERS_TIME)
        )

        fun thursday() = listOf(
            AvailableHour(from = 16, to = 17, registeredPlayers = emptyList(), type = MEMBERS_TIME),
            AvailableHour(from = 17, to = 18, registeredPlayers = emptyList(), type = MEMBERS_TIME)
        )

        fun friday() = listOf(
            AvailableHour(from = 16, to = 17, registeredPlayers = emptyList(), type = MEMBERS_TIME),
            AvailableHour(from = 17, to = 18, registeredPlayers = emptyList(), type = MEMBERS_TIME)
        )
    }
}

sealed class BookAgendaErrorDomain {
    data object MaxCapacityReached : BookAgendaErrorDomain()
    data object PlayerAlreadyBooked : BookAgendaErrorDomain()
    data object AvailableHourNotFound : BookAgendaErrorDomain()
}

sealed class CancelBookingErrorDomain {
    data object PlayerNotBooked : CancelBookingErrorDomain()
    data object AvailableHourNotFound : CancelBookingErrorDomain()
}

@JvmInline
value class Player(val name: String) {
    companion object {
        context(Raise<Error>)
        fun <Error> createOrElse(name: String, onError: () -> Error): Player {
            if (name.isBlank()) raise(onError())

            val nameParts = name.trim().split(" ")

            return if (nameParts.size >= 2) Player("${nameParts[0].first()}. ${nameParts[1]}")
            else Player(name)
        }
    }
}

typealias Year = Int

typealias Week = Int

enum class HourType { TEAM_TRAINING, ADULT_ACADEMY, KIDS_ACADEMY, MEMBERS_TIME }

data class MaxCapacity(val value: Int = 8)
