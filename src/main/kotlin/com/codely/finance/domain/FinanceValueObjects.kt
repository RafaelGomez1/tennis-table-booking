package com.codely.finance.domain

import com.codely.members.domain.Price
import java.time.YearMonth
import java.util.UUID

@JvmInline
value class FinanceRecordId(val value: UUID) {
    companion object {
        fun generate(): FinanceRecordId = FinanceRecordId(UUID.randomUUID())
    }
}

@JvmInline
value class OperationalCostId(val value: UUID) {
    companion object {
        fun generate(): OperationalCostId = OperationalCostId(UUID.randomUUID())
    }
}

@JvmInline
value class CostName(val value: String) {
    init {
        require(value.isNotBlank()) { "Cost name cannot be blank" }
    }
}

@JvmInline
value class Month(val value: YearMonth) {
    companion object {
        fun of(year: Int, month: Int): Month = Month(YearMonth.of(year, month))
        fun now(): Month = Month(YearMonth.now())
    }

    fun toYearMonth(): YearMonth = value
    fun year(): Int = value.year
    fun monthValue(): Int = value.monthValue
}

@JvmInline
value class MonthlyMemberCount(val value: Int) {
    init {
        require(value >= 0) { "Member count cannot be negative" }
    }
}

data class MonthlyIncome(val totalAmount: Price, val memberCount: MonthlyMemberCount) {
    companion object {
        fun zero(): MonthlyIncome = MonthlyIncome(Price(0), MonthlyMemberCount(0))
    }
}

data class OperationalCost(
    val id: OperationalCostId,
    val name: CostName,
    val amount: Price
) {
    companion object {
        fun create(name: CostName, amount: Price): OperationalCost =
            OperationalCost(OperationalCostId.generate(), name, amount)
    }
}

data class MonthlyExpenses(
    val coachingExpenses: Price,
    val operationalCosts: List<OperationalCost>
) {
    val totalAmount: Price
        get() = Price(coachingExpenses.value + operationalCosts.sumOf { it.amount.value })

    companion object {
        fun zero(): MonthlyExpenses = MonthlyExpenses(Price(0), emptyList())
    }
}

data class MonthlySummary(
    val income: MonthlyIncome,
    val expenses: MonthlyExpenses
) {
    val balance: Price
        get() = Price(income.totalAmount.value - expenses.totalAmount.value)
}
