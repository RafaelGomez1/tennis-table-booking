package com.codely.competition.calendar.application.search

import com.codely.competition.calendar.domain.ClubCalendar
import com.codely.competition.calendar.domain.ClubCalendarRepository
import com.codely.competition.calendar.domain.SearchClubCalendarCriteria

context(ClubCalendarRepository)
suspend fun searchClubCalendar(criteria: SearchClubCalendarCriteria): ClubCalendar? =
    search(criteria)
