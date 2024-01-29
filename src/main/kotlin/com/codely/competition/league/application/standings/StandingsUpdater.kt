package com.codely.competition.league.application.standings

import com.codely.competition.clubs.domain.Club
import com.codely.competition.clubs.domain.ClubName
import com.codely.competition.clubs.domain.ClubRepository
import com.codely.competition.clubs.domain.SearchClubCriteria.ByLeague
import com.codely.competition.league.domain.*
import com.codely.competition.league.domain.SearchLeagueCriteria.ByName
import com.codely.competition.players.application.create.BLACKLISTED_KEYWORDS
import java.util.*
import java.util.regex.Pattern

context(LeagueRepository, ClubRepository)
suspend fun updateStandings(leagueName: LeagueName, group: LeagueGroup, input: String) {
    val league = search(ByName(leagueName))
    val clubs = search(ByLeague(leagueName))

    val sanitizedList = input
        .split("\n")
        .filter { line -> !BLACKLISTED_KEYWORDS.any { it in line } }
        .filter { line -> line.isNotBlank() }

    val start = sanitizedList.indexOf(STAT_HEADER)
    val end = sanitizedList.indexOf(RESULT_HEADER)

    val leagueStandings =
        sanitizedList
            .subList(start + 1, end)
            .map { it.mapToStandings(clubs) }

    league?.updateStandings(group, leagueStandings)
        ?.let { updatedLeague -> save(updatedLeague) }

    leagueStandings.map { println(it) }
}

private fun String.mapToStandings(clubs: List<Club>): LeagueStandings {
    val input = this

    val pattern = Pattern.compile("([a-zA-Z].*[a-zA-Z])")
    val matcher = pattern.matcher(input)

    val clubName = if (matcher.find()) matcher.group(1) else ""

    val actualClubName = clubs.first { it.clubName.value.contains(clubName) }.clubName.value
    val elements = this.replace(actualClubName, "").trim().split(" ")

    val setsWon = elements[2].toInt()
    val gamesPlayed = elements[3].toInt()
    val gamesWon = elements[4].toInt()
    val gamesLost = gamesPlayed - gamesWon
    val standing = elements.first().toInt()
    val points = gamesWon * 2

    val (_, setsLost) = elements[1].getPointsAndSets(gamesWon, gamesLost)

    return LeagueStandings(
        id = UUID.randomUUID(),
        club = ClubName(value = actualClubName),
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

private const val STAT_HEADER = "CdEEGEJ PGEquip EP PP Pts"
private const val RESULT_HEADER = "1Jornada Acta1a volta"
