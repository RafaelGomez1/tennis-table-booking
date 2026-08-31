package com.codely.stats.infrastructure.scheduled

import com.codely.stats.application.calculate.CalculateStatsCommand
import com.codely.stats.application.calculate.CalculateStatsCommandHandler
import java.time.LocalDate
import kotlinx.coroutines.runBlocking
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class CalculateMonthlyStatsScheduledJob(
    private val commandHandler: CalculateStatsCommandHandler
) {
    @Scheduled(cron = "0 0 1 1 * *")
    fun execute() = runBlocking {
        val previousMonth = LocalDate.now().minusMonths(1)
        commandHandler.handle(
            CalculateStatsCommand(
                month = previousMonth.month,
                year = previousMonth.year
            )
        )
    }
}
