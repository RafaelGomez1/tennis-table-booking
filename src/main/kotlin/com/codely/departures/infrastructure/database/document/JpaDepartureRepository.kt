package com.codely.departures.infrastructure.database.document

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface JpaDepartureRepository : CoroutineCrudRepository<DepartureDocument, String>
