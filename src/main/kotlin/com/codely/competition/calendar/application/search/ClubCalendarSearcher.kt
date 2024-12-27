package com.codely.competition.calendar.application.search

import com.codely.competition.calendar.domain.ClubCalendar
import com.codely.competition.calendar.domain.ClubCalendarRepository
import com.codely.competition.calendar.domain.FindClubCalendarCriteria

context(ClubCalendarRepository)
suspend fun searchClubCalendar(criteria: FindClubCalendarCriteria): ClubCalendar? =
    find(criteria)
