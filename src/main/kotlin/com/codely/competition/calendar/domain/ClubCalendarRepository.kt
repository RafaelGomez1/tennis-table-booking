package com.codely.competition.calendar.domain

import com.codely.competition.clubs.domain.ClubName
import com.codely.competition.league.domain.LeagueName

interface ClubCalendarRepository {
    suspend fun save(calendar: ClubCalendar)
    suspend fun search(criteria: SearchClubCalendarCriteria): ClubCalendar?
}

sealed class SearchClubCalendarCriteria {
    class ByNameAndLeague(val leagueName: LeagueName, val clubName: ClubName): SearchClubCalendarCriteria()
}
