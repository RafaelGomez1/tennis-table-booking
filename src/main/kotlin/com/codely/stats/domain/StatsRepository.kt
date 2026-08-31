package com.codely.stats.domain

import java.time.Month

interface StatsRepository {
    suspend fun save(stats: AgendaStats)
    suspend fun findByMonthAndYear(month: Month, year: Int): AgendaStats?
}
