package com.codely.competition.calendar.domain

import com.codely.competition.clubs.domain.ClubName
import com.codely.competition.league.domain.LeagueName
import java.security.InvalidParameterException
import java.util.*

data class ClubCalendar(
    val id: UUID,
    val clubName: ClubName,
    val league: LeagueName,
    val matches: List<Match>,
) {
    fun updateMatches(matches: List<Match>) =
        copy(matches = matches)
}

data class Match(
    val id: String,
    val visitorClub: ClubName,
    val result: MatchResult,
    val dateTime: String,
    val isHomeGame: Boolean
)

@JvmInline
value class Games(val value: Int) {
    init {
        if (value < 0 || value > 6) throw InvalidParameterException("Matches are played up to 7 games so the number must be between 0 and 6")
    }

    operator fun compareTo(other: Games): Int =
        when {
            this.value < other.value -> -1
            this.value > other.value -> 1
            else -> 0
        }
}

sealed class MatchResult {
    data class Won(val wonGames: Games, val lostGames: Games) : MatchResult() {
        init {
            if (wonGames <= lostGames) throw InvalidParameterException("Won match must have more games won that lost")
        }
    }
    data class Lost(val wonGames: Games, val lostGames: Games) : MatchResult() {
        init {
            if (lostGames <= wonGames) throw InvalidParameterException("Won match must have more games won that lost")
        }
    }
    data object NotPlayed : MatchResult()

    companion object {
        fun create(wonGames: String, lostGames: String): MatchResult =
            when {
                wonGames == "N/A" || lostGames == "N/A" ->  NotPlayed
                wonGames.toInt() > lostGames.toInt() -> Won(Games(wonGames.toInt()), Games(lostGames.toInt()))
                lostGames.toInt() > wonGames.toInt() -> Lost(Games(wonGames.toInt()), Games(lostGames.toInt()))
                else -> NotPlayed
            }
    }

    fun name(): String =
        when(this) {
            is Lost -> "LOST"
            NotPlayed -> "NOT_PLAYED"
            is Won -> "WON"
        }
}
