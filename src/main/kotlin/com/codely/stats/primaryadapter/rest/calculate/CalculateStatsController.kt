package com.codely.stats.primaryadapter.rest.calculate

import com.codely.shared.cors.BaseController
import com.codely.shared.response.Response
import com.codely.stats.application.calculate.CalculateStatsCommand
import com.codely.stats.application.calculate.CalculateStatsCommandHandler
import java.time.Month
import kotlinx.coroutines.coroutineScope
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class CalculateStatsController(
    private val commandHandler: CalculateStatsCommandHandler
) : BaseController() {

    @PostMapping("/api/stats/calculate")
    suspend fun calculate(
        @RequestParam month: String,
        @RequestParam year: Int
    ): Response<*> = coroutineScope {
        commandHandler.handle(CalculateStatsCommand(Month.valueOf(month.uppercase()), year))
        Response.status(OK).build<Unit>()
    }
}
