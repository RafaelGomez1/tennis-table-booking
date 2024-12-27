package com.codely.competition.clubs.infrastructure.database

import com.codely.competition.clubs.domain.SearchClubCriteria
import com.codely.competition.clubs.domain.ClubExistsCriteria
import com.codely.competition.clubs.domain.Club
import com.codely.competition.clubs.domain.ClubName
import com.codely.competition.clubs.domain.ClubRepository
import com.codely.competition.clubs.domain.ClubExistsCriteria.ByNameAndLeague
import com.codely.competition.clubs.domain.SearchClubCriteria.All
import com.codely.competition.clubs.domain.SearchClubCriteria.ByLeague
import com.codely.competition.league.domain.LeagueName
import com.codely.shared.dispatcher.withIOContext
import kotlinx.coroutines.flow.toList
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Component
import java.util.UUID

interface JpaClubRepository : CoroutineCrudRepository<ClubDocument, String> {
    suspend fun existsByNameAndLeague(name: String, league: String): Boolean
    suspend fun findAllByLeague(name: String): List<ClubDocument>
}

@Document(collection = "Clubs")
data class ClubDocument(
    @Id
    val id: String,
    val name: String,
    val league: String,
    val group: String,
) {
    fun toClub(): Club = Club(ClubName(name), LeagueName.valueOf(league), id = UUID.fromString(id), group = group)
}

internal fun Club.toDocument(): ClubDocument = ClubDocument(id.toString(), clubName.value, leagueName.name, group)

@Component
class MongoClubDatabase(private val repository: JpaClubRepository) : ClubRepository {

    override suspend fun save(club: Club) {
        withIOContext {
            repository.save(club.toDocument())
        }
    }
    override suspend fun search(criteria: SearchClubCriteria): List<Club> =
        withIOContext {
            when (criteria) {
                All -> repository.findAll().toList()
                is ByLeague -> repository.findAllByLeague(criteria.leagueName.name)
            }.map { it.toClub() }
        }

    override suspend fun exists(criteria: ClubExistsCriteria): Boolean =
        withIOContext {
            when (criteria) {
                is ByNameAndLeague -> repository.existsByNameAndLeague(
                    criteria.clubName.value,
                    criteria.leagueName.name
                )
            }
        }
}
