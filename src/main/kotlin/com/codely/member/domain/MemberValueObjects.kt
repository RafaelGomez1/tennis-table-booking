package com.codely.member.domain

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
    WEDNESDAY_7_8("Wednesday", 19, 20)
}

enum class Team { TWO_A, THREE_B }

sealed class MemberType {
    data object Casual : MemberType()
    data class AcademyBeginner(val group: AcademyGroup) : MemberType()
    data object AcademyIntermediate : MemberType()
    data class Competition(val team: Team) : MemberType()
}
