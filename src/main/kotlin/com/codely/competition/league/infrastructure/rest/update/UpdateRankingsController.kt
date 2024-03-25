package com.codely.competition.league.infrastructure.rest.update

import com.codely.competition.league.application.ranking.UpdateLeagueRankingCommand
import com.codely.competition.league.application.ranking.UpdateRankingCommandHandler
import com.codely.competition.league.domain.LeagueName
import com.codely.shared.config.CompetitionConfig
import com.codely.shared.response.Response
import com.codely.shared.response.withoutBody
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.springframework.http.HttpStatus
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

    @PostMapping("/rankings")
    fun ranking(): Response<*> = runBlocking {
        val (preferente, primera, segundaA, segundaB, terceraA, terceraB) = configuration

        val urls = mapOf(
            LeagueName.PREFERENT to URL(preferente.ranking),
            LeagueName.PRIMERA to URL(primera.ranking),
            LeagueName.SEGUNDA_A to URL(segundaA.ranking),
            LeagueName.SEGUNDA_B to URL(segundaB.ranking),
            LeagueName.TERCERA_A to URL(terceraA.ranking),
            LeagueName.TERCERA_B to URL(terceraB.ranking)
        )

        urls.forEach { ( league, url) ->
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
