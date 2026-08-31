package com.codely.stats.secondaryadapter.database.document

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface JpaStatsRepository : CoroutineCrudRepository<StatsDocument, String> {
    suspend fun findByMonthAndYear(month: String, year: Int): StatsDocument?
}
