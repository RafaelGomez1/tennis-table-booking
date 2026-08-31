package com.codely.stats.application.calculate

import com.codely.agenda.domain.AgendaRepository
import com.codely.agenda.domain.AgendaSearchByCriteria.ByMonthAndYear
import com.codely.stats.domain.StatsRepository
import java.time.Month
import org.springframework.stereotype.Component

data class CalculateStatsCommand(val month: Month, val year: Int)

@Component
class CalculateStatsCommandHandler(
    private val agendaRepository: AgendaRepository,
    private val statsRepository: StatsRepository
) {
    suspend fun handle(command: CalculateStatsCommand) {
        val agendas = agendaRepository.search(ByMonthAndYear(command.month, command.year))
        val stats = calculateStats(command.month, command.year, agendas)
        statsRepository.save(stats)
    }
}
