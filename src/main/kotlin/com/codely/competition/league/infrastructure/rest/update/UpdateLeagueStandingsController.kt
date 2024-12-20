package com.codely.competition.league.infrastructure.rest.update

import com.codely.competition.calendar.domain.ClubCalendarRepository
import com.codely.competition.clubs.domain.ClubRepository
import com.codely.competition.league.application.standings.UpdateStandingsCommand
import com.codely.competition.league.application.standings.handle
import com.codely.competition.league.domain.LeagueRepository
import com.codely.shared.config.CompetitionConfig
import com.codely.shared.response.Response
import com.codely.shared.response.withoutBody
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.apache.pdfbox.text.PDFTextStripper
import org.springframework.http.HttpStatus.ACCEPTED
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URL

@RestController
class UpdateLeagueStandingsController(
    private val repository: LeagueRepository,
    private val clubRepository: ClubRepository,
    private val configuration: CompetitionConfig,
    private val calendarRepository: ClubCalendarRepository
) {

    private val textStripper = PDFTextStripper()

    @PostMapping("/api/standings")
    fun ranking(): Response<*> = runBlocking {
        val (preferente, primera, segundaA, segundaB, terceraA, terceraB, cuarta) = configuration
        val standings = listOf(preferente, primera, segundaA, segundaB, terceraA, terceraB, cuarta)

        standings.map { liga ->
            liga.results.forEach { group, url ->
                launch { processURLContent(URL(url), liga.name, group) }
            }
        }

        Response.status(ACCEPTED).withoutBody()
    }

    private suspend fun processURLContent(url: URL, league: String, group: String) {
        with(repository) {
            with(clubRepository) {
                with(calendarRepository) {
                    org.apache.pdfbox.pdmodel.PDDocument.load(url.openStream()).use { pdDocument ->
                        val text = textStripper.getText(pdDocument)
                        handle(UpdateStandingsCommand(league, group, text))
                    }
                }
            }
        }
    }
}
