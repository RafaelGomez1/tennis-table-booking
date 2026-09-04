package com.codely.departures.domain

interface DepartureRepository {
    suspend fun save(departure: Departure)
    suspend fun findAll(): List<Departure>
    suspend fun findById(id: DepartureId): Departure?
}

suspend fun DepartureRepository.save(departure: Departure): Departure =
    save(departure).let { departure }
