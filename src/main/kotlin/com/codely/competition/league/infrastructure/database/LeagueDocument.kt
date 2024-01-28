package com.codely.competition.league.infrastructure.database

import com.codely.competition.clubs.domain.ClubName
import com.codely.competition.league.domain.*
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.ZonedDateTime
import java.time.ZonedDateTime.now
import java.util.*

@Document(collection = "Ranking")
data class LeagueDocument(
    @Id
    val id: String,
    val name: String,
    val players: List<RankedPlayerDocument>,
    val standings: Map<String, List<LeagueStandingsDocument>>,
    val createdOn: ZonedDateTime = now(),
    val updatedOn: ZonedDateTime = now()
) {
    fun toLeague(): League =
        League(
            id = UUID.fromString(id),
            name = LeagueName.valueOf(name),
            rankings = players.map { it.toRankedPlayer() },
            standings = standings.toDomain(),
            createdOn = createdOn
        )
}

data class RankedPlayerDocument(
    val id: Long,
    val name: String,
    val club: String,
    val stats: GameStatsDocument,
    val rankingPoints: Int
) {
    fun toRankedPlayer(): RankedPlayer =
        RankedPlayer(
            id = id,
            name = name,
            club = club,
            stats = stats.toGameStats(),
            rankingPoints = rankingPoints
        )
}

data class GameStatsDocument(
    val gamesPlayed: Int,
    val gamesWon: Int,
    val gamesLost: Int,
    val winRate: Double
) {
    fun toGameStats(): GameStats =
        GameStats(gamesPlayed = gamesPlayed, gamesWon = gamesWon, gamesLost = gamesLost, winRate = winRate)
}

data class LeagueStandingsDocument(
    val id: String,
    val club: String,
    val gamesPlayed: Int,
    val gamesWon: Int,
    val gamesLost: Int,
    val setsWon: Int,
    val setsLost: Int,
    val points: Int,
    val standing: Int
) {
    fun toLeagueStandings(): LeagueStandings =
        LeagueStandings(
            id = UUID.fromString(id),
            club = ClubName(value = club),
            gamesPlayed = gamesPlayed,
            gamesWon = gamesWon,
            gamesLost = gamesLost,
            setsWon = setsWon,
            setsLost = setsLost,
            points = Points(points),
            standing = Standing(standing)
        )
}

internal fun LeagueStandings.toDocument(): LeagueStandingsDocument =
    LeagueStandingsDocument(
        id = id.toString(),
        club = club.value,
        gamesPlayed = gamesPlayed,
        gamesWon = gamesWon,
        gamesLost = gamesLost,
        setsWon = setsWon,
        setsLost = setsLost,
        points = points.value,
        standing = standing.value
    )

internal fun League.toDocument(): LeagueDocument =
    LeagueDocument(
        id = id.toString(),
        name = name.name,
        players = rankings.map { it.toDocument() },
        standings = standings.toDocument(),
        createdOn = createdOn,
        updatedOn = now()
    )

internal fun Map<LeagueGroup, List<LeagueStandings>>.toDocument(): Map<String, List<LeagueStandingsDocument>> =
    this.mapValues { (_, standingsList) ->
        standingsList.map { standing -> standing.toDocument() }
    }.mapKeys { it.key.toString() }

internal fun Map<String, List<LeagueStandingsDocument>>.toDomain(): Map<LeagueGroup, List<LeagueStandings>> =
    this.mapValues { (_, standingsList) ->
        standingsList.map { standing -> standing.toLeagueStandings() }
    }.mapKeys { LeagueGroup.valueOf(it.key) }

internal fun RankedPlayer.toDocument(): RankedPlayerDocument =
    RankedPlayerDocument(
        id = id,
        name = name,
        club = club,
        stats = stats.toDocument(),
        rankingPoints = rankingPoints
    )
internal fun GameStats.toDocument(): GameStatsDocument =
    GameStatsDocument(gamesPlayed = gamesPlayed, gamesWon = gamesWon, gamesLost = gamesLost, winRate = winRate)
