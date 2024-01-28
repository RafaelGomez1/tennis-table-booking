package com.codely.competition.league.application.standings

import com.codely.competition.clubs.domain.ClubName
import com.codely.competition.league.domain.*
import com.codely.competition.league.domain.SearchLeagueCriteria.ByName
import java.util.*

context(LeagueRepository)
suspend fun updateStandings(league: LeagueName, group: LeagueGroup, input: String) {
    val league = search(ByName(league))

    val leagueStandings =
        input
            .split("\n")
            .subList(6, 18)
            .map { it.mapToStandings() }

    league?.updateStandings(group, leagueStandings)
        ?.let { updatedLeague -> save(updatedLeague) }

    leagueStandings.map { println(it) }
}

private fun String.mapToStandings(): LeagueStandings {
    val input = this
    val clubName = input.replace(Regex("[0-9]"), "").trim()
    val elements = this.replace(clubName, "").trim().split(" ")

    val setsWon = elements[2].toInt()
    val gamesPlayed = elements[3].toInt()
    val gamesWon = elements[4].toInt()
    val gamesLost = gamesPlayed - gamesWon
    val standing = elements.first().toInt()
    val points = gamesWon * 2

    val (_, setsLost) = elements[1].getPointsAndSets(gamesWon, gamesLost)

    return LeagueStandings(
        id = UUID.randomUUID(),
        club = ClubName(value = clubName),
        gamesPlayed = gamesPlayed,
        gamesWon = gamesWon,
        gamesLost = gamesLost,
        setsWon = setsWon,
        setsLost = setsLost,
        points = Points(value = points),
        standing = Standing(standing)
    )
}

private fun String.getPointsAndSets(gamesWon: Int, gamesLost: Int): Pair<Int, Int> {

    val points =
        if (gamesWon >= 5) this.take(2)
        else this.take(1)

    val pointlessInput = this.removePrefix(points)
    val setsLost = pointlessInput.removeSuffix(gamesLost.toString())

    return Pair(points.toInt(), setsLost.toInt())
}
