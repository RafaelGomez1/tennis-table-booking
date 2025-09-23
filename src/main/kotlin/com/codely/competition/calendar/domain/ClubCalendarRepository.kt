package com.codely.competition.calendar.domain

import com.codely.competition.clubs.domain.ClubName
import com.codely.competition.league.domain.LeagueName

interface ClubCalendarRepository {
    suspend fun save(calendar: ClubCalendar)
    suspend fun find(criteria: FindClubCalendarCriteria): ClubCalendar?
    suspend fun search(criteria: SearchClubCalendarCriteria): List<ClubCalendar>
}

sealed class FindClubCalendarCriteria {
    class ByClubNameAndLeague(val leagueName: LeagueName, val clubName: ClubName) : FindClubCalendarCriteria()
}

sealed class SearchClubCalendarCriteria {
    class ByClubNameAndLeague(val leagueName: LeagueName) : SearchClubCalendarCriteria()
}
