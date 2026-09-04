package com.codely.departures.primaryadapter.rest

import com.codely.departures.domain.Departure

data class DepartureResponseDTO(
    val id: String,
    val memberId: String,
    val name: String,
    val surname: String,
    val phoneNumbers: List<String>,
    val type: String,
    val departureDate: String,
    val academyGroups: List<String>,
    val team: String?,
    val idNumber: String?,
    val address: String?,
    val city: String?,
    val postalCode: String?,
    val dateOfBirth: String?,
    val email: String?,
    val memberSince: String?,
    val age: Int?,
    val ageGroup: String?
) {
    companion object {
        fun fromDomain(departure: Departure): DepartureResponseDTO =
            DepartureResponseDTO(
                id = departure.id.value.toString(),
                memberId = departure.memberId.value.toString(),
                name = departure.name.value,
                surname = departure.surname.value,
                phoneNumbers = departure.phoneNumbers.values.map { it.value },
                type = departure.type.toName(),
                departureDate = departure.departureDate.value.toString(),
                academyGroups = departure.type.academyGroups(),
                team = departure.type.team(),
                idNumber = departure.idNumber?.value,
                address = departure.address?.value,
                city = departure.city?.value,
                postalCode = departure.postalCode?.value,
                dateOfBirth = departure.dateOfBirth?.value?.toString(),
                email = departure.email?.value,
                memberSince = departure.memberSince?.value?.toString(),
                age = departure.age,
                ageGroup = departure.resolvedAgeGroup()?.name
            )
    }
}
