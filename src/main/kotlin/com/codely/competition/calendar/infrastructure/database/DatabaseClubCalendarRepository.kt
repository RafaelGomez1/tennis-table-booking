package com.codely.competition.calendar.infrastructure.database

import com.codely.competition.calendar.domain.ClubCalendar
import com.codely.competition.calendar.domain.ClubCalendarRepository
import com.codely.competition.calendar.domain.FindClubCalendarCriteria
import com.codely.competition.calendar.domain.FindClubCalendarCriteria.ByClubNameAndLeague
import com.codely.competition.calendar.domain.SearchClubCalendarCriteria
import com.codely.shared.dispatcher.withIOContext
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Component

@Component
class DatabaseClubCalendarRepository(private val repository: JpaClubCalendarRepository) : ClubCalendarRepository {
    override suspend fun save(calendar: ClubCalendar) {
        withIOContext {
            repository.save(calendar.toDocument())
        }
    }

    override suspend fun find(criteria: FindClubCalendarCriteria): ClubCalendar? =
        withIOContext {
            when (criteria) {
                is ByClubNameAndLeague ->
                    repository.findByClubNameAndLeague(criteria.clubName.value, criteria.leagueName.name)
            }?.toDomain()
        }

    override suspend fun search(criteria: SearchClubCalendarCriteria): List<ClubCalendar> =
        withIOContext {
            when (criteria) {
                is SearchClubCalendarCriteria.ByClubNameAndLeague ->
                    repository.findAllByLeague(criteria.leagueName.name)
            }.map { it.toDomain() }
    }
}

interface JpaClubCalendarRepository : CoroutineCrudRepository<ClubCalendarDocument, String> {
    suspend fun findByClubNameAndLeague(clubName: String, league: String): ClubCalendarDocument?
    suspend fun findAllByLeague(league: String): List<ClubCalendarDocument>
}
