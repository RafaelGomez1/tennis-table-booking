package com.codely.competition.league.application.standings

import com.codely.competition.calendar.domain.ClubCalendarRepository
import com.codely.competition.clubs.domain.ClubRepository
import com.codely.competition.league.domain.LeagueGroup
import com.codely.competition.league.domain.LeagueName
import com.codely.competition.league.domain.LeagueRepository


context(LeagueRepository, ClubRepository, ClubCalendarRepository)
suspend fun handle(query: UpdateStandingsCommand) {
    updateStandings(LeagueName.valueOf(query.league), LeagueGroup.fromString(query.group), query.input)
}


data class UpdateStandingsCommand(val league: String, val group: String, val input: String)
