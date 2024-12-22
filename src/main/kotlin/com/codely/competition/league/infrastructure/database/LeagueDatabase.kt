package com.codely.competition.league.infrastructure.database

import com.codely.competition.clubs.domain.ClubName
import com.codely.competition.league.domain.League
import com.codely.competition.league.domain.LeagueName
import com.codely.competition.league.domain.LeagueRepository
import com.codely.competition.league.domain.SearchLeagueCriteria
import com.codely.competition.league.domain.SearchLeagueCriteria.ByName
import com.codely.competition.league.domain.SearchLeagueCriteria.ByNameAndClub
import com.codely.shared.dispatcher.withIOContext
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Component

interface JpaLeagueRepository : CoroutineCrudRepository<LeagueDocument, String> {
    suspend fun deleteByName(name: String)
    suspend fun findByName(name: String): LeagueDocument?
}

@Component
class MongoLeagueRepository(private val repository: JpaLeagueRepository) : LeagueRepository {
    override suspend fun save(league: League) {
        withIOContext {
            repository.save(league.toDocument())
        }
    }

    override suspend fun delete(leagueName: LeagueName) {
        withIOContext {
            repository.deleteByName(leagueName.name)
        }
    }
    override suspend fun search(criteria: SearchLeagueCriteria): League? =
        withIOContext {
            when (criteria) {
                is ByNameAndClub -> repository.findByName(criteria.leagueName.name).filterClub(criteria.clubName)
                is ByName -> repository.findByName(criteria.leagueName.name)
            }?.toLeague()
        }

    private fun LeagueDocument?.filterClub(name: ClubName): LeagueDocument? =
        this?.copy(players = players.filter { it.club == name.value })
}
