package com.codely.competition.calendar.infrastructure.database

import com.codely.competition.calendar.domain.ClubCalendar
import com.codely.competition.calendar.domain.Games
import com.codely.competition.calendar.domain.Match
import com.codely.competition.calendar.domain.MatchResult
import com.codely.competition.calendar.domain.MatchResult.Won
import com.codely.competition.calendar.domain.MatchResult.Lost
import com.codely.competition.calendar.domain.MatchResult.NotPlayed
import com.codely.competition.clubs.domain.ClubName
import com.codely.competition.league.domain.LeagueName
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID

@Document(collection = "Club Calendar")
data class ClubCalendarDocument(
    @Id
    val id: String,
    val clubName: String,
    val league: String,
    val matches: List<MatchDocument>,
) {
    fun toDomain(): ClubCalendar =
        ClubCalendar(
            id = UUID.fromString(id),
            clubName = ClubName(value = clubName),
            league = LeagueName.valueOf(league),
            matches = matches.map { it.toDomain() }
        )
}

data class MatchDocument(
    val id: String,
    val visitorClub: String,
    val result: MatchResultDocument,
    val dateTime: String,
    val isHomeGame: Boolean
) {
    fun toDomain(): Match = Match(
        id = id,
        visitorClub = ClubName(value = visitorClub),
        result = matchResultFrom(
            name = result.name,
            wonGames = result.gamesWon,
            lostGames = result.gamesLost
        ),
        dateTime = dateTime,
        isHomeGame = isHomeGame
    )
}

data class MatchResultDocument(
    val name: String,
    val gamesWon: Int,
    val gamesLost: Int
)

internal fun matchResultFrom(name: String, wonGames: Int, lostGames: Int): MatchResult =
    when (name) {
        "LOST" -> Lost(Games(wonGames), Games(lostGames))
        "WON" -> Won(Games(wonGames), Games(lostGames))
        else -> NotPlayed
    }

internal fun ClubCalendar.toDocument(): ClubCalendarDocument =
    ClubCalendarDocument(
        id = id.toString(),
        clubName = clubName.value,
        league = league.name,
        matches = matches.map { it.toDocument() }
    )

internal fun Match.toDocument(): MatchDocument =
    MatchDocument(
        id = id,
        visitorClub = visitorClub.value,
        result = result.toDocument(),
        dateTime = dateTime,
        isHomeGame = isHomeGame
    )

internal fun MatchResult.toDocument(): MatchResultDocument =
    when (this) {
        is Lost -> MatchResultDocument(name = name(), wonGames.value, lostGames.value)
        NotPlayed -> MatchResultDocument(name = name(), 0, 0)
        is Won -> MatchResultDocument(name = name(), wonGames.value, lostGames.value)
    }
