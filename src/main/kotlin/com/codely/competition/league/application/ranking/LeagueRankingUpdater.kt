package com.codely.competition.league.application.ranking

import com.codely.competition.clubs.domain.ClubName
import com.codely.competition.clubs.domain.ClubRepository
import com.codely.competition.clubs.domain.SearchClubCriteria.ByLeague
import com.codely.competition.players.application.create.BLACKLISTED_KEYWORDS
import com.codely.competition.players.domain.FindPlayerCriteria.ByClubLeagueAndName
import com.codely.competition.players.domain.PlayerRepository
import com.codely.competition.league.domain.*
import com.codely.competition.league.domain.SearchLeagueCriteria.ByName
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.time.ZonedDateTime
import java.util.*

class LeagueRankingUpdater(
    private val playerRepository: PlayerRepository,
    private val clubRepository: ClubRepository,
    private val leagueRepository: LeagueRepository
) {
    
    suspend operator fun invoke(lines: List<String>, leagueName: LeagueName) = coroutineScope {
        val sanitizedList = lines
            .filter { line -> !BLACKLISTED_KEYWORDS.any { it in line } }
            .filter { line -> line.isNotBlank() }

        val clubs = clubRepository.search(ByLeague(leagueName)).map { it.clubName.value }

        val rankedPlayers = sanitizedList
            .map { async { mapToPlayer(it, clubs, leagueName) } }
            .awaitAll()

        leagueRepository.search(ByName(leagueName)) ?: League.create(id = UUID.randomUUID(), name = leagueName, players = emptyList(), standings = mapOf(), createdOn = ZonedDateTime.now())
            .updateRankings(rankedPlayers)
            .let { updatedLeague -> leagueRepository.save(updatedLeague) }
    }

    private suspend fun mapToPlayer(input: String, clubs: List<String>, leagueName: LeagueName): RankedPlayer {
        val club = clubs.first { it in input }
        val playerName = findPlayerName(input, clubs)
        val player = playerRepository.find(ByClubLeagueAndName(ClubName(club), leagueName, playerName))
        
        return player?.let {
            val gameStats = findGameStats(input)
            val ranking = input.split(" ")[0].replaceFirst(player.id.toString(), "").toInt()
            RankedPlayer(it.id, it.name, it.clubName.value, gameStats, ranking)
                .also { ranked -> println("Player found, creating ranking $ranked") }
        } ?: createRankedPlayerFromData(input, club, playerName, clubs)
    }
    
    private fun createRankedPlayerFromData(input: String, club: String, playerName: String, clubs: List<String>): RankedPlayer {
        val updatedInput = input.formatInput(clubs)
        val elements = updatedInput.split(" ")
        val gameStats = findGameStats(input)

        val (id, ranking) = findRankingAndId(elements[0], gameStats.winRate)

        return RankedPlayer(id, playerName, club, gameStats, ranking)
    }

    private fun findPlayerName(input: String, clubs: List<String>): String {
        val clubName = clubs.first { it in input }

        val splittedRow = input
            .substringAfter(clubName)
            .trim()
            .split(" ")

        val indexOfNumbers = splittedRow.indexOfFirst { it.contains("-?\\d+(\\.\\d+)?".toRegex()) }

        return splittedRow
            .take(indexOfNumbers)
            .reversed()
            .joinToString { it }
            .uppercase()
            .replace(",", "")
    }

    private fun findGameStats(input: String): GameStats {
        val statInput = input.split(" ")
            .takeLast(4)
            .joinToString { it }

        val x = statInput
            .split(",")[0]
            .split("%")

        val winRate = x[0].toDouble()
        val games = x[1]

        val gameStats =
            when {
                games.length >= 4 -> Pair(games.takeLast(2).toInt(), games.take(2).toInt())
                games.length == 3 -> Pair(games.takeLast(2).toInt(), games.take(1).toInt())
                else -> Pair(games.takeLast(1).toInt(), games.take(1).toInt())
            }

        return GameStats(gameStats.first, gameStats.second, gameStats.gamesLost(), winRate)
    }

    private fun Pair<Int, Int>.gamesLost(): Int = this.first - this.second

    private fun findRankingAndId(input: String, winRate: Double): Pair<Long, Int> {
        // TODO -> Replace with DB call
        val id = PLAYER_IDS_OVER_THOUSAND.find { it in input }

        if (winRate == 0.0) return Pair(input.takeLast(input.length - 2).toLong(), input.take(2).toInt())

        return id
            ?.let { Pair(it.toLong(), input.take(4).toInt()) }
            ?: Pair(input.takeLast(input.length - 3).toLong(), input.take(3).toInt())
    }

    private fun String.formatInput(clubs: List<String>): String {
        val clubName = clubs.first { it in this }
        return this.replace(clubName, "$clubName ")
    }

    private val PLAYER_IDS_OVER_THOUSAND = listOf("456", "582")
}
