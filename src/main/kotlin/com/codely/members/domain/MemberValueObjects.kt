package com.codely.members.domain

import java.time.LocalDate
import java.time.Period
import java.util.UUID

@JvmInline
value class MemberId(val value: UUID) {
    companion object {
        fun generate(): MemberId = MemberId(UUID.randomUUID())
    }
}

@JvmInline
value class MemberName(val value: String) {
    init {
        require(value.isNotBlank()) { "Member name cannot be blank" }
    }
}

@JvmInline
value class MemberSurname(val value: String) {
    init {
        require(value.isNotBlank()) { "Member surname cannot be blank" }
    }
}

@JvmInline
value class ContactPhoneNumber(val value: String) {
    init {
        require(value.isNotBlank()) { "Phone number cannot be blank" }
    }
}

data class ContactPhoneNumbers(val values: List<ContactPhoneNumber>) {
    init {
        require(values.isNotEmpty()) { "At least one phone number is required" }
        require(values.size <= 2) { "A member can have at most 2 phone numbers" }
    }

    companion object {
        fun of(first: ContactPhoneNumber, second: ContactPhoneNumber? = null): ContactPhoneNumbers =
            ContactPhoneNumbers(listOfNotNull(first, second))
    }
}

enum class AcademyGroup(val day: String, val from: Int, val to: Int) {
    MONDAY_6_7("Monday", 18, 19),
    MONDAY_7_8("Monday", 19, 20),
    WEDNESDAY_6_7("Wednesday", 18, 19),
    WEDNESDAY_7_8("Wednesday", 19, 20),
    FRIDAY_6_7("Friday", 18, 19),
}

enum class Team { TWO_A, THREE_B }

sealed class Membership {
    data class Casual(val price: Price = Price(18)) : Membership()
    data class AcademyBeginner(val groups: List<AcademyGroup>, val price: Price = Price(18)) : Membership() {
        fun priceBasedOnHours(): Price =
            when {
                groups.size == 4 -> Price(30)
                groups.size == 3 -> Price(25)
                groups.size == 2 -> Price(20)
                groups.size == 1 -> Price(18)
                else -> Price(18)
            }
    }
    data class AcademyIntermediate(val price: Price = Price(18)) : Membership()
    data class Competition(val team: Team, val price: Price = Price(25)) : Membership()

    fun academyGroups(): List<String> = when (this) {
        is AcademyBeginner -> groups.map { it.name }
        else -> emptyList()
    }

    fun team(): String? = when (this) {
        is Competition -> team.name
        else -> null
    }

    fun toName(): String =
        when (this) {
            is Casual -> "CASUAL"
            is AcademyBeginner -> "ACADEMY_BEGINNER"
            is AcademyIntermediate -> "ACADEMY_INTERMEDIATE"
            is Competition -> "COMPETITION"
        }

    fun membership(): Price =
        when (this) {
            is Casual -> price
            is AcademyBeginner -> priceBasedOnHours()
            is AcademyIntermediate -> price
            is Competition -> price
        }

    companion object {
        fun fromFilter(value: String): Membership? =
            when (value.uppercase()) {
                "CASUAL" -> Casual()
                "ACADEMY_BEGINNER" -> AcademyBeginner(listOf(AcademyGroup.MONDAY_6_7))
                "ACADEMY_INTERMEDIATE" -> AcademyIntermediate()
                "COMPETITION" -> Competition(Team.TWO_A)
                else -> null
            }

        fun fromString(value: String, academyGroups: List<String>? = null, team: String? = null): Membership =
            when (value.uppercase()) {
                "CASUAL" -> Casual()
                "ACADEMY_BEGINNER" -> AcademyBeginner(academyGroups!!.map { AcademyGroup.valueOf(it) })
                "ACADEMY_INTERMEDIATE" -> AcademyIntermediate()
                "COMPETITION" -> Competition(Team.valueOf(team!!))
                else -> throw IllegalArgumentException("Unknown MemberType: $value")
            }
    }
}

@JvmInline
value class IDNumber(val value: String) {
    init {
        require(value.isNotBlank()) { "ID number cannot be blank" }
    }
}

@JvmInline
value class MemberAddress(val value: String) {
    init {
        require(value.isNotBlank()) { "Address cannot be blank" }
    }
}

@JvmInline
value class MemberCity(val value: String) {
    init {
        require(value.isNotBlank()) { "City cannot be blank" }
    }
}

@JvmInline
value class MemberPostalCode(val value: String) {
    init {
        require(value.isNotBlank()) { "Postal code cannot be blank" }
    }
}

@JvmInline
value class MemberEmail(val value: String) {
    init {
        require(value.isNotBlank()) { "Email cannot be blank" }
        require(value.contains("@")) { "Email must contain @" }
    }
}

data class MemberDateOfBirth(val value: LocalDate) {
    fun age(): Int = Period.between(value, LocalDate.now()).years
}

data class MemberSince(val value: LocalDate)

enum class AgeGroup {
    KIDS,
    SENIORS,
    RETIRED;

    companion object {
        fun fromAge(age: Int): AgeGroup = when {
            age < 18 -> KIDS
            age <= 65 -> SENIORS
            else -> RETIRED
        }
    }
}

@JvmInline
value class Price(val value: Int) {
    init {
        require(value >= 0) { "Price must be greater than or equal to 0" }
    }
}
