package com.codely.departures.infrastructure.database

import com.codely.departures.domain.Departure
import com.codely.departures.domain.DepartureId
import com.codely.departures.domain.DepartureRepository
import com.codely.departures.infrastructure.database.document.JpaDepartureRepository
import com.codely.departures.infrastructure.database.document.toDocument
import com.codely.shared.dispatcher.withIOContext
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Component

@Component
class MongoDepartureRepository(private val repository: JpaDepartureRepository) : DepartureRepository {

    override suspend fun save(departure: Departure) {
        withIOContext {
            repository.save(departure.toDocument())
        }
    }

    override suspend fun findAll(): List<Departure> =
        withIOContext {
            repository.findAll()
                .toList()
                .map { it.toDeparture() }
        }

    override suspend fun findById(id: DepartureId): Departure? =
        withIOContext {
            repository.findById(id.value.toString())?.toDeparture()
        }
}
