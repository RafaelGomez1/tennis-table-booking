package com.codely.competition.players.infrastructure.subscriber

import com.codely.competition.players.application.create.UpdatePlayerCommand
import com.codely.competition.players.application.create.UpdatePlayersCommandHandler
import com.codely.shared.config.CompetitionConfig
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.net.URL

@Component
class ProcessClubPlayerSubscriber(
    private val updater: UpdatePlayersCommandHandler,
    private val configuration: CompetitionConfig
) {
    private val textStripper = PDFTextStripper()

    @Scheduled(cron = "0 0 9 * * MON-TUE")
    fun invoke() = runBlocking {
        val (preferente, primera, segundaA, segundaB, terceraA, terceraB, cuarta) = configuration
        val ligas = listOf(preferente, primera, segundaA, segundaB, terceraA, terceraB, cuarta)

        ligas.map { liga ->
            liga.players.forEach { key, value ->
                launch {
                    processURLContent(group = key, url = URL(value), league = liga.name)
                }
            }
        }
    }

    private suspend fun processURLContent(group: String, url: URL, league: String) {
        PDDocument.load(url.openStream()).use { pdDocument ->
            val text = textStripper.getText(pdDocument).split("\n")
            updater.handle(UpdatePlayerCommand(group, text, league))
        }
    }
}
