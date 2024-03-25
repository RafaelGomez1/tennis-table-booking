package com.codely.agenda.fakes

import com.codely.agenda.domain.Agenda
import com.codely.agenda.domain.AgendaFindByCriteria
import com.codely.agenda.domain.AgendaFindByCriteria.ById
import com.codely.agenda.domain.AgendaRepository
import com.codely.agenda.domain.AgendaSearchByCriteria
import com.codely.agenda.domain.AgendaSearchByCriteria.ByWeekAndYear
import com.codely.shared.fakes.FakeRepository
import java.util.*

class FakeAgendaRepository : AgendaRepository, FakeRepository<UUID, Agenda> {
    override val elements = mutableMapOf<UUID, Agenda>()
    override val errors= mutableListOf<Throwable>()

    override suspend fun save(agenda: Agenda) { elements.saveOrUpdate(agenda, agenda.id) }

    override suspend fun search(criteria: AgendaSearchByCriteria): List<Agenda> =
        when (criteria) {
            is ByWeekAndYear -> elements.values.filter { el -> el.week == criteria.week }
        }

    override suspend fun find(criteria: AgendaFindByCriteria): Agenda? =
        when (criteria) {
            is ById -> elements[criteria.id]
        }
}
