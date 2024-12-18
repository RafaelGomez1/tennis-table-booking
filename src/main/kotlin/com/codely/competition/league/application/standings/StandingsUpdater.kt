package com.codely.competition.league.application.standings

import com.codely.competition.calendar.domain.*
import com.codely.competition.calendar.domain.SearchClubCalendarCriteria.ByNameAndLeague
import com.codely.competition.clubs.domain.Club
import com.codely.competition.clubs.domain.ClubName
import com.codely.competition.clubs.domain.ClubRepository
import com.codely.competition.clubs.domain.SearchClubCriteria.ByLeague
import com.codely.competition.league.domain.*
import com.codely.competition.league.domain.SearchLeagueCriteria.ByName
import com.codely.competition.players.application.create.BLACKLISTED_KEYWORDS
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.regex.Pattern

context(LeagueRepository, ClubRepository, ClubCalendarRepository)
suspend fun updateStandings(leagueName: LeagueName, group: LeagueGroup, input: String) {
    val league = search(ByName(leagueName)) ?: League.create(id = UUID.randomUUID(), name = leagueName, players = emptyList(), standings = mapOf(), createdOn = ZonedDateTime.now())
    val clubs = search(ByLeague(leagueName))

    println("Starting update standings and calendar from ${league.name} and group ${group.name}")

    val sanitizedList = input
        .split("\n")
        .filter { line -> !BLACKLISTED_KEYWORDS.any { it in line } }
        .filter { line -> line.isNotBlank() }

    val clubsCalendar = sanitizedList
        .obtainClubCalendar("Jornada Acta", clubs, leagueName)
        .filter { it.matches.isNotEmpty() }

    val start = sanitizedList.indexOf(STAT_HEADER)
    val end = sanitizedList.indexOf(RESULT_HEADER)

    val leagueStandings =
        sanitizedList
            .subList(start + 1, end)
            .map { it.mapToStandings(clubs) }

    save(league.updateStandings(group, leagueStandings))

    clubsCalendar.forEach { clubCalendar ->
        val actualCalendar = search(ByNameAndLeague(clubName = clubCalendar.clubName, leagueName = leagueName))

        val clubCalendarToSave = actualCalendar
            ?.updateMatches(clubCalendar.matches)
            ?: clubCalendar

        save(clubCalendarToSave)
    }
}

private fun List<String>.obtainClubCalendar(tag: String, clubs: List<Club>, leagueName: LeagueName): List<ClubCalendar> {
    val resultMap = mutableMapOf<Int, List<String>>()
    val resultList = mutableListOf<String>()
    var segmentCount = 1
    var startIndex = -1


    for ((index, element) in this.withIndex()) {
        if (element.contains(tag)) {
            if (startIndex != -1) {

                val segment = this.subList(startIndex + 1, index)
                resultMap[segmentCount] = segment
                resultList += segment
                segmentCount++
            }

            startIndex = index
        }
    }

    val clubCalendars = clubs
        .map { club ->
            val matches = resultList
                .filter { !it.contains("DESCANSA") }
                .filter { it.contains(club.clubName.value) }
                .map { row ->

                    val dateTime =
                        // This is done by a bug in the original dataset which points to 2024
                        if (row.contains("1933")) "16/02/25"
                        else extractDate(row)

                    val updatedRow =
                        row
                            .replace(club.clubName.value, "")
                            .replace(dateTime, "")

                    val rivalTeam = updatedRow.extractTeamName(clubs)

                    val isTeamLocal =
                        row
                            .replace(dateTime, "")
                            .isLocalTeamBeforeRival(localTeam = club.clubName.value, rivalTeam = rivalTeam)

                    val rowWithoutTeams =
                        updatedRow.replace(rivalTeam, "")

                    val (gamesWon, gamesLost) =
                        if (dateTime.toLocalDate().isAfter(LocalDate.now())) Pair("N/A", "N/A")
                        else rowWithoutTeams.extractFirstTwoNumbers(isTeamLocal)

                    val result = MatchResult.create(gamesWon, gamesLost)

                    Match(
                        id = UUID.randomUUID().toString(),
                        visitorClub = ClubName(value = rivalTeam.trimIndent()),
                        result = result,
                        dateTime = dateTime,
                        isHomeGame = isTeamLocal
                    )
                }

            ClubCalendar(id = UUID.randomUUID(), clubName = club.clubName, matches = matches, league = leagueName)
        }

    return clubCalendars
}

private fun String.isLocalTeamBeforeRival(localTeam: String, rivalTeam: String): Boolean {
    // Find the index positions of both teams in the string
    val localTeamIndex = this.indexOf(localTeam, ignoreCase = true)
    val rivalTeamIndex = this.indexOf(rivalTeam, ignoreCase = true)

    // If either of the team names are not found in the string, return false
    if (localTeamIndex == -1 || rivalTeamIndex == -1) {
        return false
    }

    // Return true if the local team appears before the rival team
    return localTeamIndex < rivalTeamIndex
}

private fun String.extractFirstTwoNumbers(isTeamLocal: Boolean): Pair<String, String> {
    val singleDigitRegex = """\d""".toRegex() // Matches individual digits anywhere
    val matches = singleDigitRegex.findAll(this).take(2).map { it.value }.toList()

    return when {
        isTeamLocal && matches.size == 2 -> Pair(matches[0], matches[1])
        !isTeamLocal && matches.size == 2 -> Pair(matches[1], matches[0])
        else -> Pair("N/A", "N/A")
    }
}

private fun String.toLocalDate(): LocalDate {
    val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yy")
    return LocalDate.parse(this, dateFormat) // Returns a LocalDate object
}

private fun String.extractTeamName(clubs: List<Club>): String =
    clubs.first { it.clubName.value in this }.clubName.value

private fun extractDate(data: String): String {
    val dateRegex = """\d{2}/\d{2}/\d{2}""".toRegex() // Regex to match "dd/MM/yy"

    val match = dateRegex.find(data)
    return match?.value ?: data
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

    return Pair(
        if (points.isNumeric()) points.toInt() else 0,
        if (setsLost.isNumeric()) setsLost.toInt() else 0
    )
}

private fun String.isNumeric() = this.matches("-?\\d+(\\.\\d+)?".toRegex())

private const val STAT_HEADER = "CdEEGEJ PGEquip EP PP Pts"
private const val RESULT_HEADER = "1Jornada Acta1a volta"
