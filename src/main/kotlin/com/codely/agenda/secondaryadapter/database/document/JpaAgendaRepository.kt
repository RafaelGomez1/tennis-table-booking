package com.codely.agenda.secondaryadapter.database.document

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface JpaAgendaRepository : CoroutineCrudRepository<AgendaDocument, String> {
    suspend fun findAllByWeekAndYear(week: Int, year: Int): List<AgendaDocument>
}
