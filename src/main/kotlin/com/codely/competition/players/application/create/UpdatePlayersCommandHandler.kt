package com.codely.competition.players.application.create

import com.codely.competition.clubs.application.create.ClubsCreator
import com.codely.competition.clubs.domain.ClubName
import com.codely.competition.clubs.domain.ClubRepository
import com.codely.competition.league.domain.LeagueName
import com.codely.competition.players.domain.Player
import com.codely.competition.players.domain.PlayerRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component

data class UpdatePlayerCommand(
    val group: String,
    val playerListText: List<String>,
    val league: String
)

@Component
class UpdatePlayersCommandHandler(
    repository: PlayerRepository,
    clubRepository: ClubRepository
) {

    private val createClubs = ClubsCreator(clubRepository)
    private val createPlayers = PlayerCreator(repository)

    suspend fun handle(command: UpdatePlayerCommand) = coroutineScope {
        val sanitizedList = command.playerListText.subList(4, command.playerListText.size)
            .filter { line -> !BLACKLISTED_KEYWORDS.any { it in line } }

        val leagueName = LeagueName.valueOf(command.league)

        val clubs = sanitizedList.filter { it.isNotEmpty() && !it.first().isDigit() }

        val groupedPlayers = groupByClub(sanitizedList, clubs, leagueName)

        groupedPlayers.values.map { clubPlayers -> launch { createPlayers(clubPlayers) }.join() }

        val domainClubs = clubs.map { ClubName(it) }
        createClubs(command.group, domainClubs, leagueName)
    }

    private fun groupByClub(inputList: List<String>, clubNames: List<String>, leagueName: LeagueName): Map<String, List<Player>> {
        val result = mutableMapOf<String, MutableList<Player>>()
        var currentClubName = ""

        for (input in inputList) {
            if (clubNames.any { input.startsWith(it) }) {
                currentClubName = input
                result[currentClubName] = mutableListOf()
            } else if (currentClubName.isNotEmpty() && input.isNotBlank()) {
                val player = mapToPlayer(input, currentClubName, leagueName)
                result[currentClubName]?.add(player)
            }
        }

        return result
    }

    private fun mapToPlayer(input: String, clubName: String, leagueName: LeagueName): Player {
        var splitInput = input.split(" ")
            .filter { it.isNotBlank() }

        val promotedToHigherLeagues = LeagueName.parseNames().any { it in splitInput.last() }
        val id = splitInput[0]
        val initialRanking = splitInput.getInitialRanking().toInt()
        splitInput = input.replace(id, "")
            .trim()
            .split(" ")
            .filter { !it.contains("/") }

        return Player.create(
            id = id.toLong(),
            name = splitInput.joinToString(" ").uppercase(),
            club = clubName,
            initialRanking = initialRanking,
            leagueName = leagueName,
            promotedToHigherLeagues = promotedToHigherLeagues
        )
    }

    private fun List<String>.getInitialRanking(): String {
        val last = last()
        return when {
            LeagueName.parseNames().any { it in last } -> get(size - 2).removeInscriptionDate()
            last.contains("/") -> last.removeInscriptionDate()
            else -> last()
        }
    }

    private fun String.removeInscriptionDate(): String =
        when {
            contains("/") -> removeRange(this.length - 8, this.length)
            else -> this
        }
}

val BLACKLISTED_KEYWORDS = listOf(
    "Representació Territorial",
    "Federació Catalana",
    "C/Duquessa d'Orleans",
    "RK Inicial",
    "Rànquing Actualitzat",
    "Només es mostren els jugadors que han disputat més de 4 partits i que no han pujat de categoria. A l'absolut hi són tots",
    "PuntsPGPJ PP % JG JP %"
)
