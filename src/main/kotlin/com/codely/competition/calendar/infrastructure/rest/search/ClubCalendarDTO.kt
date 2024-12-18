package com.codely.competition.calendar.infrastructure.rest.search

import com.codely.competition.calendar.domain.ClubCalendar
import com.codely.competition.calendar.domain.Match
import com.codely.competition.calendar.domain.MatchResult
import com.codely.competition.calendar.domain.MatchResult.Won
import com.codely.competition.calendar.domain.MatchResult.Lost
import com.codely.competition.calendar.domain.MatchResult.NotPlayed

data class ClubCalendarDTO(
    val id: String,
    val clubName: String,
    val league: String,
    val matches: List<MatchDTO>,
)

data class MatchDTO(
    val id: String,
    val visitorClub: String,
    val result: MatchResultDTO,
    val dateTime: String,
    val isHomeGame: Boolean
)

data class MatchResultDTO(
    val name: String,
    val gamesWon: Int,
    val gamesLost: Int
)

internal fun ClubCalendar.toDTO(): ClubCalendarDTO =
    ClubCalendarDTO(
        id = id.toString(),
        clubName = clubName.value,
        league = league.name,
        matches = matches.map { it.toDTO() }
    )

internal fun Match.toDTO(): MatchDTO =
    MatchDTO(
        id = id,
        visitorClub = visitorClub.value,
        result = result.toDTO(),
        dateTime = dateTime,
        isHomeGame = isHomeGame
    )

internal fun MatchResult.toDTO(): MatchResultDTO =
    when(this) {
        is Lost -> MatchResultDTO(name = name(), wonGames.value, lostGames.value)
        NotPlayed -> MatchResultDTO(name = name(), 0, 0)
        is Won -> MatchResultDTO(name = name(), wonGames.value, lostGames.value)
    }
