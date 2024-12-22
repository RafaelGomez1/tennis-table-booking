package com.codely.agenda.secondaryadapter.database

import com.codely.agenda.domain.Agenda
import com.codely.agenda.domain.AgendaFindByCriteria
import com.codely.agenda.domain.AgendaFindByCriteria.ById
import com.codely.agenda.domain.AgendaRepository
import com.codely.agenda.domain.AgendaSearchByCriteria
import com.codely.agenda.domain.AgendaSearchByCriteria.ByWeekAndYear
import com.codely.agenda.secondaryadapter.database.document.JpaAgendaRepository
import com.codely.agenda.secondaryadapter.database.document.toDocument
import com.codely.shared.dispatcher.withIOContext
import org.springframework.stereotype.Component

@Component
class MongoAgendaRepository(private val repository: JpaAgendaRepository) : AgendaRepository {

    override suspend fun save(agenda: Agenda) {
        withIOContext {
            repository.save(agenda.toDocument())
        }
    }

    override suspend fun search(criteria: AgendaSearchByCriteria): List<Agenda> =
        withIOContext {
            when (criteria) {
                is ByWeekAndYear -> repository.findAllByWeekAndYear(criteria.week, criteria.year)
            }.map { document -> document.toAgenda() }
        }

    override suspend fun find(criteria: AgendaFindByCriteria): Agenda? =
        withIOContext {
            when (criteria) {
                is ById -> repository.findById(criteria.id.toString())
            }?.toAgenda()
        }

}
