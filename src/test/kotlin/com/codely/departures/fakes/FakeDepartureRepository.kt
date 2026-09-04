package com.codely.departures.fakes

import com.codely.departures.domain.Departure
import com.codely.departures.domain.DepartureId
import com.codely.departures.domain.DepartureRepository
import com.codely.shared.fakes.FakeRepository

class FakeDepartureRepository : DepartureRepository, FakeRepository<DepartureId, Departure> {
    override val elements = mutableMapOf<DepartureId, Departure>()
    override val errors = mutableListOf<Throwable>()

    override suspend fun save(departure: Departure) {
        failIfConfiguredOrElse {
            elements.saveOrUpdate(departure, departure.id)
        }
    }

    override suspend fun findAll(): List<Departure> =
        elements.values.toList()

    override suspend fun findById(id: DepartureId): Departure? =
        elements[id]
}
