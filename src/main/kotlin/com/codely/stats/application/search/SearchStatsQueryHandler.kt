package com.codely.stats.application.search

import com.codely.stats.domain.AgendaStats
import com.codely.stats.domain.StatsRepository
import java.time.Month
import org.springframework.stereotype.Component

data class SearchStatsQuery(val month: Month, val year: Int)

@Component
class SearchStatsQueryHandler(private val statsRepository: StatsRepository) {
    suspend fun handle(query: SearchStatsQuery): AgendaStats? =
        statsRepository.findByMonthAndYear(query.month, query.year)
}
