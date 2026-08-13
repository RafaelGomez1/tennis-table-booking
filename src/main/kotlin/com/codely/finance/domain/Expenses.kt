package com.codely.finance.domain

import com.codely.members.domain.Price



sealed class CoachingExpenses {

    abstract fun weeklyExpense(): Price

    data class BeginnerCoach(val hourlyPrice: Price = Price(20)) : CoachingExpenses() {
        override fun weeklyExpense(): Price {
            return Price(hourlyPrice.value * 6)
        }
    }
    data class IntermediateCoach(val hourlyPrice: Price = Price(18)) : CoachingExpenses() {
        override fun weeklyExpense(): Price {
            return Price(hourlyPrice.value * 2)
        }
    }
    data class CompetititionCoach(val hourlyPrice: Price = Price(18)) : CoachingExpenses() {
        override fun weeklyExpense(): Price {
            return Price(hourlyPrice.value * 2)
        }
    }
}
