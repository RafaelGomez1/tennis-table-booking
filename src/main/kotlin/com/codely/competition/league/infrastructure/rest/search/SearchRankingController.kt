package com.codely.competition.league.infrastructure.rest.search

import com.codely.competition.league.application.search.SearchLeagueQuery
import com.codely.competition.league.application.search.handle
import com.codely.competition.league.domain.LeagueRepository
import com.codely.competition.league.infrastructure.rest.error.LeagueRankingServerErrors.LEAGUE_RANKING_DOES_NOT_EXIST
import com.codely.shared.cors.BaseController
import com.codely.shared.response.Response
import com.codely.shared.response.withBody
import kotlinx.coroutines.coroutineScope
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URLDecoder
import java.nio.charset.StandardCharsets.UTF_8

@RestController
class SearchRankingController(private val repository: LeagueRepository): BaseController() {

    @GetMapping("/api/rankings")
    suspend fun search(@RequestParam league: String, @RequestParam club: String): Response<*> = coroutineScope {
        with(repository) {
            handle(SearchLeagueQuery(league, club.decodedParameter()))
                ?.let { ranking -> Response.status(OK).body(ranking.toDTO()) }
                ?: Response.status(NOT_FOUND).withBody(LEAGUE_RANKING_DOES_NOT_EXIST)
        }
    }

    @GetMapping("/api/leagues")
    suspend fun searchLeagues(@RequestParam league: String, @RequestParam club: String?): Response<*> = coroutineScope {
        with(repository) {
            handle(SearchLeagueQuery(league, club?.decodedParameter()))
                ?.let { ranking -> Response.status(OK).body(ranking.toDTO()) }
                ?: Response.status(NOT_FOUND).withBody(LEAGUE_RANKING_DOES_NOT_EXIST)
        }
    }

    private fun String.decodedParameter(): String = URLDecoder.decode(this, UTF_8)
}
