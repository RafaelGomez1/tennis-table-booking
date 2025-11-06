package com.codely.competition.league.infrastructure.scheduled

import com.codely.competition.league.application.ranking.UpdateLeagueRankingCommand
import com.codely.competition.league.application.ranking.UpdateRankingCommandHandler
import com.codely.competition.league.domain.LeagueName.CUARTA
import com.codely.competition.league.domain.LeagueName.PREFERENT
import com.codely.competition.league.domain.LeagueName.PRIMERA
import com.codely.competition.league.domain.LeagueName.SEGUNDA_A
import com.codely.competition.league.domain.LeagueName.SEGUNDA_B
import com.codely.competition.league.domain.LeagueName.TERCERA_A
import com.codely.competition.league.domain.LeagueName.TERCERA_B
import com.codely.shared.config.CompetitionConfig
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.net.URL

@Component
class UpdateRankingsScheduledJob(
    private val updater: UpdateRankingCommandHandler,
    private val configuration: CompetitionConfig
) {

    private val textStripper = PDFTextStripper()

    @Scheduled(cron = "0 0 10 * * MON-TUE")
    fun execute() = runBlocking {
        val (preferente, primera, segundaA, segundaB, terceraA, terceraB, cuarta) = configuration

        val urls = mapOf(
            PREFERENT to URL(preferente.ranking),
            PRIMERA to URL(primera.ranking),
            SEGUNDA_A to URL(segundaA.ranking),
            SEGUNDA_B to URL(segundaB.ranking),
            TERCERA_A to URL(terceraA.ranking),
            TERCERA_B to URL(terceraB.ranking),
            CUARTA to URL(cuarta.ranking)
        )

        urls.forEach { (league, url) ->
            println("Starting ranking updating for ${league.name} at ${url.path}")
            processURLContent(url, league.name)
        }
    }

    private suspend fun processURLContent(url: URL, league: String) {
        url.openStream().use { stream ->
            PDDocument.load(stream).use { pdDocument ->
                val text = textStripper.getText(pdDocument).split("\n")
                updater.handle(UpdateLeagueRankingCommand(text, league))
            }
        }
    }
}
