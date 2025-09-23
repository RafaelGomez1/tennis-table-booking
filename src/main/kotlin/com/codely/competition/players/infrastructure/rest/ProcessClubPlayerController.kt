package com.codely.competition.players.infrastructure.rest

import com.codely.competition.players.application.create.UpdatePlayerCommand
import com.codely.competition.players.application.create.UpdatePlayersCommandHandler
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
class ProcessClubPlayerController(
    private val updater: UpdatePlayersCommandHandler,
    private val configuration: CompetitionConfig
) {
    private val textStripper = PDFTextStripper()

    @PostMapping("/api/players")
    suspend fun process(): Response<*> = coroutineScope {
        val (preferente, primera, segundaA, segundaB, terceraA, terceraB, cuarta) = configuration
        val ligas = listOf(preferente, primera, segundaA, segundaB, terceraA, terceraB, cuarta)

        ligas.map { liga ->
            liga.players.forEach { key, value ->
                launch {
                    processURLContent(group = key, url = URL(value), league = liga.name)
                }
            }
        }

        Response.status(ACCEPTED).withoutBody()
    }

    private suspend fun processURLContent(group: String, url: URL, league: String) {
        PDDocument.load(url.openStream()).use { pdDocument ->
            val text = textStripper.getText(pdDocument).split("\n")
            updater.handle(UpdatePlayerCommand(group, text, league))
        }
    }
}
