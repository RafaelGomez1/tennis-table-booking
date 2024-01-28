package com.codely.competition.league.infrastructure.rest.search

import com.codely.competition.league.domain.GameStats
import com.codely.competition.league.domain.League
import com.codely.competition.league.domain.RankedPlayer

data class LeagueRankingDocument(
    val id: String,
    val name: String,
    val players: List<RankedPlayerDocument>
)

data class RankedPlayerDocument(
    val id: Long,
    val name: String,
    val club: String,
    val stats: GameStatsDocument,
    val rankingPoints: Int
)

data class GameStatsDocument(
    val gamesPlayed: Int,
    val gamesWon: Int,
    val gamesLost: Int,
    val winRate: Double
)

internal fun GameStats.toDocument(): GameStatsDocument =
    GameStatsDocument(gamesPlayed = gamesPlayed, gamesWon = gamesWon, gamesLost = gamesLost, winRate = winRate)

internal fun RankedPlayer.toDocument(): RankedPlayerDocument =
    RankedPlayerDocument(id = id, name = name, club = club, stats = stats.toDocument(), rankingPoints = rankingPoints)

internal fun League.toDocument(): LeagueRankingDocument =
    LeagueRankingDocument(
        id = id.toString(),
        name = name.name,
        players = rankings.map { it.toDocument() }
    )

