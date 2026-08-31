package com.codely.agenda

import com.codely.agenda.domain.AvailableHour
import com.codely.agenda.domain.HourType
import com.codely.agenda.domain.HourType.MEMBERS_TIME
import com.codely.agenda.domain.MaxCapacity
import com.codely.agenda.domain.Player
import java.util.*

object AvailableHourMother {

    fun fullPlayerList(
        id: UUID = UUID.randomUUID(),
        from: Int = 16,
        to: Int = 17,
        capacity: MaxCapacity = MaxCapacity(8),
        type: HourType = MEMBERS_TIME,
        players: List<Player> = fullPlayerList
    ) = listOf(AvailableHour(id, from, to, capacity, type, players))

    private val fullPlayerList = listOf(
        Player("Antonio"),
        Player("Marcel"),
        Player("Tino"),
        Player("Txus"),
        Player("Exposito"),
        Player("Carlos"),
        Player("Lucas"),
        Player("Maria"),
    )
}
