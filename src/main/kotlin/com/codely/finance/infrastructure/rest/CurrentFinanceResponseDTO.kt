package com.codely.finance.infrastructure.rest

import com.codely.finance.application.CurrentFinanceSnapshot

data class CurrentFinanceResponseDTO(
    val totalBalance: Int,
    val income: IncomeResponseDTO,
    val expenses: ExpensesResponseDTO
)

data class IncomeResponseDTO(
    val total: Int,
    val memberCount: Int,
    val breakdown: Map<String, Int>
)

data class ExpensesResponseDTO(
    val total: Int
)

fun CurrentFinanceSnapshot.toResponseDTO(): CurrentFinanceResponseDTO =
    CurrentFinanceResponseDTO(
        totalBalance = totalBalance,
        income = IncomeResponseDTO(
            total = income.total,
            memberCount = income.memberCount,
            breakdown = income.breakdown
        ),
        expenses = ExpensesResponseDTO(
            total = expenses.total
        )
    )
