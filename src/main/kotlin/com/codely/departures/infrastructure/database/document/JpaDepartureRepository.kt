package com.codely.departures.infrastructure.database.document

import org.springframework.data.mongodb.repository.MongoRepository

interface JpaDepartureRepository : MongoRepository<DepartureDocument, String>
