package com.codely.competition.league.infrastructure.database

import com.codely.competition.clubs.domain.ClubName
import com.codely.competition.league.domain.*
import com.codely.competition.league.domain.SearchLeagueCriteria.ByName
import com.codely.competition.league.domain.SearchLeagueCriteria.ByNameAndClub
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component

interface JpaLeagueRepository : MongoRepository<LeagueDocument, String> {
    fun deleteByName(name: String)
    fun findByName(name: String): LeagueDocument?
}

@Component
class MongoLeagueRepository(private val repository: JpaLeagueRepository): LeagueRepository {
    override suspend fun save(league: League) { repository.save(league.toDocument()) }
    override suspend fun delete(leagueName: LeagueName) { repository.deleteByName(leagueName.name) }
    override suspend fun search(criteria: SearchLeagueCriteria): League? =
        when(criteria) {
            is ByNameAndClub -> repository.findByName(criteria.leagueName.name).filterClub(criteria.clubName)
            is ByName -> repository.findByName(criteria.leagueName.name)
        }?.toLeague()

    private fun LeagueDocument?.filterClub(name: ClubName): LeagueDocument? =
        this?.copy(players = players.filter { it.club == name.value })
}
