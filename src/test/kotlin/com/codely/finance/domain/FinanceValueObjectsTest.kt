package com.codely.finance.domain

import com.codely.members.domain.Price
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class FinanceValueObjectsTest : DescribeSpec({
    describe("MonthlyMemberCount") {
        it("should create valid member count") {
            val count = MonthlyMemberCount(10)
            count.value shouldBe 10
        }

        it("should throw on negative count") {
            shouldThrow<IllegalArgumentException> {
                MonthlyMemberCount(-1)
            }
        }

        it("should allow zero members") {
            val count = MonthlyMemberCount(0)
            count.value shouldBe 0
        }
    }

    describe("MonthlyIncome") {
        it("should calculate total income correctly") {
            val income = MonthlyIncome(Price(1000), MonthlyMemberCount(10))
            income.totalAmount.value shouldBe 1000
            income.memberCount.value shouldBe 10
        }

        it("should return zero income") {
            val zero = MonthlyIncome.zero()
            zero.totalAmount.value shouldBe 0
            zero.memberCount.value shouldBe 0
        }
    }

    describe("MonthlyExpenses") {
        it("should calculate total expenses with only coaching") {
            val expenses = MonthlyExpenses(Price(500), emptyList())
            expenses.totalAmount.value shouldBe 500
        }

        it("should calculate total with operational costs") {
            val operationalCosts = listOf(
                OperationalCost.create(CostName("Rent"), Price(300)),
                OperationalCost.create(CostName("Utilities"), Price(100))
            )
            val expenses = MonthlyExpenses(Price(500), operationalCosts)
            expenses.totalAmount.value shouldBe 900
        }

        it("should return zero expenses") {
            val zero = MonthlyExpenses.zero()
            zero.totalAmount.value shouldBe 0
        }
    }

    describe("MonthlySummary") {
        it("should calculate balance correctly") {
            val income = MonthlyIncome(Price(2000), MonthlyMemberCount(10))
            val expenses = MonthlyExpenses(Price(500), emptyList())
            val summary = MonthlySummary(income, expenses)

            summary.balance.value shouldBe 1500
        }

        it("should have zero balance when expenses equal income") {
            val income = MonthlyIncome(Price(1000), MonthlyMemberCount(10))
            val expenses = MonthlyExpenses(Price(1000), emptyList())
            val summary = MonthlySummary(income, expenses)

            summary.balance.value shouldBe 0
        }
    }

    describe("OperationalCost") {
        it("should create operational cost") {
            val cost = OperationalCost.create(CostName("Maintenance"), Price(250))
            cost.name.value shouldBe "Maintenance"
            cost.amount.value shouldBe 250
        }

        it("should throw on blank cost name") {
            shouldThrow<IllegalArgumentException> {
                CostName("  ")
            }
        }
    }

    describe("Month") {
        it("should create month of year") {
            val month = Month.of(2024, 8)
            month.year() shouldBe 2024
            month.monthValue() shouldBe 8
        }
    }
})
