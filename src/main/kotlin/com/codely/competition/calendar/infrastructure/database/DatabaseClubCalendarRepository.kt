package com.codely.competition.calendar.infrastructure.database

import com.codely.competition.calendar.domain.ClubCalendar
import com.codely.competition.calendar.domain.ClubCalendarRepository
import com.codely.competition.calendar.domain.SearchClubCalendarCriteria
import com.codely.competition.calendar.domain.SearchClubCalendarCriteria.ByNameAndLeague
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

    override suspend fun search(criteria: SearchClubCalendarCriteria): ClubCalendar? =
        withIOContext {
            when (criteria) {
                is ByNameAndLeague ->
                    repository.findByClubNameAndLeague(criteria.clubName.value, criteria.leagueName.name)
            }?.toDomain()
        }
}

interface JpaClubCalendarRepository : CoroutineCrudRepository<ClubCalendarDocument, String> {
    suspend fun findByClubNameAndLeague(clubName: String, league: String): ClubCalendarDocument?
}
