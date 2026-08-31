package com.codely.stats.secondaryadapter.database

import com.codely.shared.dispatcher.withIOContext
import com.codely.stats.domain.AgendaStats
import com.codely.stats.domain.StatsRepository
import com.codely.stats.secondaryadapter.database.document.JpaStatsRepository
import com.codely.stats.secondaryadapter.database.document.toDocument
import java.time.Month
import org.springframework.stereotype.Component

@Component
class MongoStatsRepository(private val repository: JpaStatsRepository) : StatsRepository {

    override suspend fun save(stats: AgendaStats) {
        withIOContext {
            val existing = repository.findByMonthAndYear(stats.month.name, stats.year)
            val document = stats.toDocument().let { doc ->
                existing?.let { doc.copy(id = it.id) } ?: doc
            }
            repository.save(document)
        }
    }

    override suspend fun findByMonthAndYear(month: Month, year: Int): AgendaStats? =
        withIOContext {
            repository.findByMonthAndYear(month.name, year)?.toAgendaStats()
        }
}

private fun com.codely.stats.secondaryadapter.database.document.StatsDocument.copy(id: String) =
    com.codely.stats.secondaryadapter.database.document.StatsDocument(
        id = id,
        month = month,
        year = year,
        slots = slots
    )
