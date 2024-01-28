package com.codely.competition.league.domain

import com.codely.competition.clubs.domain.ClubName
import com.codely.competition.players.domain.PlayerId
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.ZonedDateTime.now
import java.util.UUID

data class League(
    val id: UUID,
    val name: LeagueName,
    val rankings: List<RankedPlayer>,
    val standings: Map<LeagueGroup, List<LeagueStandings>>,
    val createdOn: ZonedDateTime
) {
    companion object {
        fun create(
            id: UUID = UUID.randomUUID(),
            name: LeagueName,
            players: List<RankedPlayer>,
            standings: Map<LeagueGroup, List<LeagueStandings>>,
            createdOn: ZonedDateTime = now()
        ) = League(id, name, players, standings, createdOn)
    }

    fun updateStandings(group: LeagueGroup, standingList: List<LeagueStandings>): League {
        val updatedStandings = standings.toMutableMap()
        updatedStandings[group] = standingList
        return copy(standings = updatedStandings)
    }

    fun updateRankings(newRankings: List<RankedPlayer>): League = copy(rankings = newRankings)
}

data class RankedPlayer(
    val id: PlayerId,
    val name: String,
    val club: String,
    val stats: GameStats,
    val rankingPoints: Int
)

data class GameStats(
    val gamesPlayed: Int,
    val gamesWon: Int,
    val gamesLost: Int,
    val winRate: Double
)

data class LeagueStandings(
    val id: UUID,
    val club: ClubName,
    val gamesPlayed: Int,
    val gamesWon: Int,
    val gamesLost: Int,
    val setsWon: Int,
    val setsLost: Int,
    val points: Points,
    val standing: Standing
)


