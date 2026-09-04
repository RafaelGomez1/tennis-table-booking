package com.codely.departures.domain

import java.time.LocalDate
import java.util.UUID

@JvmInline
value class DepartureId(val value: UUID) {
    companion object {
        fun generate() = DepartureId(UUID.randomUUID())
    }
}

@JvmInline
value class DepartureDate(val value: LocalDate)
