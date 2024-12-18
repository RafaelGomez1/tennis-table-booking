package com.codely.competition.calendar.infrastructure.rest.search

import com.codely.competition.calendar.application.search.SearchClubCalendarQuery
import com.codely.competition.calendar.application.search.handle
import com.codely.competition.calendar.domain.ClubCalendarRepository
import com.codely.competition.calendar.infrastructure.error.ClubCalendarRankingServerErrors.CLUB_CALENDAR_DOES_NOT_EXIST
import com.codely.shared.cors.BaseController
import com.codely.shared.response.Response
import com.codely.shared.response.withBody
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SearchClubCalendarController(private val repository: ClubCalendarRepository): BaseController() {

    @GetMapping("/club-calendar")
    fun search(@RequestParam league: String, @RequestParam club: String): Response<*> = runBlocking {
        with(repository) {
            handle(SearchClubCalendarQuery(league = league, club = club))
                ?.let { calendar -> Response.status(OK).body(calendar.toDTO()) }
                ?: Response.status(NOT_FOUND).withBody(CLUB_CALENDAR_DOES_NOT_EXIST)
        }
    }
}
