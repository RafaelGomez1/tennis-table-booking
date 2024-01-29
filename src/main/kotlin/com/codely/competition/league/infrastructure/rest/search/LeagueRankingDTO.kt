package com.codely.competition.league.infrastructure.rest.search

import com.codely.competition.league.domain.*

data class LeagueRankingDTO(
    val id: String,
    val name: String,
    val players: List<RankedPlayerDTO>,
    val standings: Map<String, List<LeagueStandingsDTO>>
)

data class RankedPlayerDTO(
    val id: Long,
    val name: String,
    val club: String,
    val stats: GameStatsDTO,
    val rankingPoints: Int
)

data class GameStatsDTO(
    val gamesPlayed: Int,
    val gamesWon: Int,
    val gamesLost: Int,
    val winRate: Double
)

data class LeagueStandingsDTO(
    val id: String,
    val club: String,
    val gamesPlayed: Int,
    val gamesWon: Int,
    val gamesLost: Int,
    val setsWon: Int,
    val setsLost: Int,
    val points: Int,
    val standing: Int
)

internal fun GameStats.toDTO(): GameStatsDTO =
    GameStatsDTO(gamesPlayed = gamesPlayed, gamesWon = gamesWon, gamesLost = gamesLost, winRate = winRate)

internal fun RankedPlayer.toDTO(): RankedPlayerDTO =
    RankedPlayerDTO(id = id, name = name, club = club, stats = stats.toDTO(), rankingPoints = rankingPoints)

internal fun Map<LeagueGroup, List<LeagueStandings>>.toDTO(): Map<String, List<LeagueStandingsDTO>> =
    this.mapValues { (_, standingsList) ->
        standingsList.map { standing -> standing.toDTO() }
    }.mapKeys { it.key.toString() }

internal fun LeagueStandings.toDTO(): LeagueStandingsDTO =
    LeagueStandingsDTO(
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

internal fun League.toDTO(): LeagueRankingDTO =
    LeagueRankingDTO(
        id = id.toString(),
        name = name.name,
        players = rankings.map { it.toDTO() },
        standings = standings.toDTO()
    )

