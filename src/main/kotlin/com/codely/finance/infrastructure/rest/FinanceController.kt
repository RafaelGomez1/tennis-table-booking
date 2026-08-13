package com.codely.finance.infrastructure.rest

import com.codely.finance.application.GetCurrentFinanceBalance
import com.codely.shared.cors.BaseController
import com.codely.shared.response.Response
import kotlinx.coroutines.coroutineScope
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class FinanceController(
    private val getCurrentFinanceBalance: GetCurrentFinanceBalance,
) : BaseController() {

    @GetMapping("/api/finances/current")
    suspend fun getCurrentFinance(): Response<*> = coroutineScope {
        val snapshot = getCurrentFinanceBalance.calculate()
        Response.status(OK).body(snapshot.toResponseDTO())
    }
}
