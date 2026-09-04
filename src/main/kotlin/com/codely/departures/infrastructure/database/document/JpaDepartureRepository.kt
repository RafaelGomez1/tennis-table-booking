package com.codely.departures.infrastructure.database.document

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface JpaDepartureRepository : MongoRepository<DepartureDocument, String>
