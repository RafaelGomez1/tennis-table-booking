package com.codely.competition.calendar.infrastructure.database

import com.codely.competition.calendar.domain.ClubCalendar
import com.codely.competition.calendar.domain.ClubCalendarRepository
import com.codely.competition.calendar.domain.SearchClubCalendarCriteria
import com.codely.competition.calendar.domain.SearchClubCalendarCriteria.ByNameAndLeague
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component

@Component
class DatabaseClubCalendarRepository(private val repository: JpaClubCalendarRepository): ClubCalendarRepository {
    override suspend fun save(calendar: ClubCalendar) {
        withContext(Dispatchers.IO) {
            repository.save(calendar.toDocument())
        }
    }

    override suspend fun search(criteria: SearchClubCalendarCriteria): ClubCalendar? =
        when(criteria) {
            is ByNameAndLeague -> withContext(Dispatchers.IO) {
                repository.findByClubNameAndLeague(criteria.clubName.value, criteria.leagueName.name)
            }
        }?.toDomain()
    }

interface JpaClubCalendarRepository : MongoRepository<ClubCalendarDocument, String> {
    fun findByClubNameAndLeague(clubName: String, league: String): ClubCalendarDocument?
}
