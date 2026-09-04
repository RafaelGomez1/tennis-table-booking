package com.codely.departures.application.create

import arrow.core.raise.Raise
import arrow.core.raise.catch
import com.codely.departures.domain.Departure
import com.codely.departures.domain.DepartureDate
import com.codely.departures.domain.DepartureRepository
import com.codely.members.domain.Member
import java.time.LocalDate

context(DepartureRepository, Raise<CreateDepartureError>)
suspend fun handle(command: CreateDepartureCommand): Departure {
    val departureDate = catch({ DepartureDate(LocalDate.parse(command.departureDateStr)) }) {
        raise(CreateDepartureError.InvalidDateFormat)
    }

    val departure = Departure.fromMember(command.member, departureDate)
    save(departure)

    return departure
}

data class CreateDepartureCommand(
    val member: Member,
    val departureDateStr: String
)

sealed class CreateDepartureError {
    data object InvalidDateFormat : CreateDepartureError()
}
