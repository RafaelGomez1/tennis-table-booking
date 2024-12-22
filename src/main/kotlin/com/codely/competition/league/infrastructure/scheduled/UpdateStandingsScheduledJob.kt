package com.codely.competition.league.infrastructure.scheduled

import com.codely.competition.calendar.domain.ClubCalendarRepository
import com.codely.competition.clubs.domain.ClubRepository
import com.codely.competition.league.application.standings.UpdateStandingsCommand
import com.codely.competition.league.application.standings.handle
import com.codely.competition.league.domain.LeagueRepository
import com.codely.shared.config.CompetitionConfig
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.net.URL

@Component
class UpdateStandingsScheduledJob(
    private val repository: LeagueRepository,
    private val clubRepository: ClubRepository,
    private val configuration: CompetitionConfig,
    private val calendarRepository: ClubCalendarRepository
) {
    private val textStripper = PDFTextStripper()

    @Scheduled(cron = "0 10 10 * * MON-TUE")
    fun execute() = runBlocking {
        val (preferente, primera, segundaA, segundaB, terceraA, terceraB, cuarta) = configuration
        val standings = listOf(preferente, primera, segundaA, segundaB, terceraA, terceraB, cuarta)

        standings.map { liga ->
            liga.results.forEach { group, url ->
                println("Starting ranking updating for ${liga.name}")
                launch { processURLContent(URL(url), liga.name, group) }
            }
        }
    }

    private suspend fun processURLContent(url: URL, league: String, group: String) {
        with(repository) {
            with(clubRepository) {
                with(calendarRepository) {
                    url.openStream().use { stream ->
                        PDDocument.load(stream).use { pdDocument ->
                            val text = textStripper.getText(pdDocument)
                            handle(UpdateStandingsCommand(league, group, text))
                        }
                    }
                }
            }
        }
    }
}
