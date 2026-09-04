package com.codely.departures.application.create

import arrow.core.raise.Raise
import arrow.core.raise.catch
import com.codely.departures.domain.Departure
import com.codely.departures.domain.DepartureDate
import com.codely.departures.domain.DepartureRepository
import com.codely.members.domain.Member
import java.time.LocalDate

context(DepartureRepository, Raise<CreateDepartureError>)
suspend fun createDeparture(member: Member, departureDateStr: String): Departure {
    val departureDate = catch({ DepartureDate(LocalDate.parse(departureDateStr)) }) {
        raise(CreateDepartureError.InvalidDateFormat)
    }

    val departure = Departure.fromMember(member, departureDate)
    save(departure)

    return departure
}

sealed class CreateDepartureError {
    data object InvalidDateFormat : CreateDepartureError()
}
