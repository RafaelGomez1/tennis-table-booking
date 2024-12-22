package com.codely.competition.league.infrastructure.rest.update

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
import com.codely.shared.response.Response
import com.codely.shared.response.withoutBody
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.springframework.http.HttpStatus.ACCEPTED
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URL

@RestController
class UpdateRankingsController(
    private val updater: UpdateRankingCommandHandler,
    private val configuration: CompetitionConfig
) {

    private val textStripper = PDFTextStripper()

    @PostMapping("/api/rankings")
    suspend fun ranking(): Response<*> = coroutineScope {
        val (preferente, primera, segundaA, segundaB, terceraA, terceraB, cuarta) = configuration

        val urls = mapOf(
            PREFERENT to URL(preferente.ranking),
            PRIMERA to URL(primera.ranking),
            SEGUNDA_A to URL(segundaA.ranking),
            SEGUNDA_B to URL(segundaB.ranking),
            TERCERA_A to URL(terceraA.ranking),
            TERCERA_B to URL(terceraB.ranking),
            CUARTA to URL(cuarta.ranking),
        )

        urls.forEach { (league, url) ->
            launch { processURLContent(url, league.name) }.join()
        }

        Response.status(ACCEPTED).withoutBody()
    }

    private suspend fun processURLContent(url: URL, league: String) {
        PDDocument.load(url.openStream()).use { pdDocument ->
            val text = textStripper.getText(pdDocument).split("\n")
            updater.handle(UpdateLeagueRankingCommand(text, league))
        }
    }
}
