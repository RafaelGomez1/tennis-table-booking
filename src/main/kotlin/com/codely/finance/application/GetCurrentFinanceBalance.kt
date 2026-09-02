package com.codely.finance.application

import com.codely.finance.domain.CoachingExpenses
import com.codely.finance.domain.CoachingExpenses.BeginnerCoach
import com.codely.finance.domain.CoachingExpenses.IntermediateCoach
import com.codely.members.domain.Member
import com.codely.members.domain.MemberRepository
import com.codely.members.domain.MemberSearchByCriteria
import com.codely.members.domain.Membership.AcademyBeginner
import com.codely.members.domain.Membership.AcademyIntermediate
import com.codely.members.domain.Membership.Casual
import com.codely.members.domain.Membership.Coach
import com.codely.members.domain.Membership.Competition
import com.codely.members.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class GetCurrentFinanceBalance(
    private val memberRepository: MemberRepository,
) {

    suspend fun calculate(): CurrentFinanceSnapshot {
        val allMembers = memberRepository.search(
            MemberSearchByCriteria.All,
            PageRequest(page = 0, size = 300)
        ).content

        val incomeBreakdown = calculateIncomeBreakdown(allMembers)
        val totalIncome = incomeBreakdown.values.sum()

        val expensesBreakdown = calculateExpensesBreakdown()
        val totalExpenses = expensesBreakdown.values.sum()
        val totalBalance = totalIncome - totalExpenses

        return CurrentFinanceSnapshot(
            totalBalance = totalBalance,
            income = IncomeSnapshot(
                total = totalIncome,
                memberCount = allMembers.size,
                breakdown = incomeBreakdown
            ),
            expenses = ExpensesSnapshot(
                total = totalExpenses,
                breakdown = expensesBreakdown
            )
        )
    }

    private fun calculateIncomeBreakdown(
        members: List<Member>
    ): Map<String, Int> {
        val breakdown = mutableMapOf(
            "casual" to 0,
            "academyBeginner" to 0,
            "academyIntermediate" to 0,
            "competition" to 0
        )

        members.forEach { member ->
            when (member.type) {
                is Casual -> {
                    breakdown["casual"] = breakdown["casual"]!! + member.type.membership().value
                }
                is AcademyBeginner -> {
                    breakdown["academyBeginner"] =
                        breakdown["academyBeginner"]!! + member.type.membership().value
                }
                is AcademyIntermediate -> {
                    breakdown["academyIntermediate"] =
                        breakdown["academyIntermediate"]!! + member.type.membership().value
                }
                is Competition -> {
                    breakdown["competition"] =
                        breakdown["competition"]!! + member.type.membership().value
                }
                is Coach -> {}
            }
        }

        return breakdown
    }

    private fun calculateExpensesBreakdown(): Map<String, Int> {
        val breakdown = mapOf(
            "BeginnerCoaching" to BeginnerCoach().monthlyExpense().value,
            "IntermediateCoaching" to IntermediateCoach().monthlyExpense().value,
            "CompetitionCoaching" to CoachingExpenses.CompetitionCoach().monthlyExpense().value,
        )

        return breakdown
    }
}

data class CurrentFinanceSnapshot(
    val totalBalance: Int,
    val income: IncomeSnapshot,
    val expenses: ExpensesSnapshot
)

data class IncomeSnapshot(
    val total: Int,
    val memberCount: Int,
    val breakdown: Map<String, Int>
)

data class ExpensesSnapshot(
    val total: Int,
    val breakdown: Map<String, Int>
)
