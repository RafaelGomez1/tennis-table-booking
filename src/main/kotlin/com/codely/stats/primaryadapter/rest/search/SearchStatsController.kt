package com.codely.stats.primaryadapter.rest.search

import com.codely.shared.cors.BaseController
import com.codely.shared.response.Response
import com.codely.stats.application.search.SearchStatsQuery
import com.codely.stats.application.search.SearchStatsQueryHandler
import java.time.Month
import kotlinx.coroutines.coroutineScope
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SearchStatsController(private val queryHandler: SearchStatsQueryHandler) : BaseController() {

    @GetMapping("/api/stats")
    suspend fun search(
        @RequestParam month: String,
        @RequestParam year: Int
    ): Response<*> = coroutineScope {
        val stats = queryHandler.handle(SearchStatsQuery(Month.valueOf(month.uppercase()), year))

        stats
            ?.let { Response.status(OK).body(StatsResponseDTO.from(it)) }
            ?: Response.status(NOT_FOUND).build()
    }
}
