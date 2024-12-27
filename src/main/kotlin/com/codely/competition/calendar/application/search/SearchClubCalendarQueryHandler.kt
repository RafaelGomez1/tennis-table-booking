package com.codely.competition.calendar.application.search

import com.codely.competition.calendar.domain.ClubCalendar
import com.codely.competition.calendar.domain.ClubCalendarRepository
import com.codely.competition.calendar.domain.FindClubCalendarCriteria.ByClubNameAndLeague
import com.codely.competition.clubs.domain.ClubName
import com.codely.competition.league.domain.LeagueName

context(ClubCalendarRepository)
suspend fun handle(query: SearchClubCalendarQuery): ClubCalendar? {
    val leagueName = LeagueName.valueOf(query.league)
    val clubName = ClubName(query.club)
    return searchClubCalendar(ByClubNameAndLeague(clubName = clubName, leagueName = leagueName))
}

data class SearchClubCalendarQuery(val league: String, val club: String)
