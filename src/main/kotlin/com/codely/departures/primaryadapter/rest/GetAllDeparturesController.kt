package com.codely.departures.primaryadapter.rest

import com.codely.departures.domain.DepartureRepository
import com.codely.shared.cors.BaseController
import com.codely.shared.response.Response
import kotlinx.coroutines.coroutineScope
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class GetAllDeparturesController(
    private val repository: DepartureRepository
) : BaseController() {

    @GetMapping("/api/departures")
    suspend fun getAllDepartures(): Response<*> =
        coroutineScope {
            val departures = repository.findAll()
            val dtos = departures.map { DepartureResponseDTO.fromDomain(it) }
            Response.status(OK).body(dtos)
        }
}
