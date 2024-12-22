package com.codely.competition.players.infrastructure.database

import com.codely.competition.clubs.domain.ClubName
import com.codely.competition.league.domain.LeagueName
import com.codely.competition.players.domain.ExistsPlayerCriteria
import com.codely.competition.players.domain.FindPlayerCriteria
import com.codely.competition.players.domain.FindPlayerCriteria.ByClubAndName
import com.codely.competition.players.domain.FindPlayerCriteria.ByClubLeagueAndName
import com.codely.competition.players.domain.FindPlayerCriteria.ById
import com.codely.competition.players.domain.Player
import com.codely.competition.players.domain.PlayerRepository
import com.codely.competition.players.domain.SearchPlayerCriteria
import com.codely.competition.players.domain.SearchPlayerCriteria.ByClub
import com.codely.shared.dispatcher.withIOContext
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Component

interface JpaPlayerRepository : CoroutineCrudRepository<PlayerDocument, Long> {
    suspend fun findAllByClub(club: String): List<PlayerDocument>
    suspend fun findByClubContainingIgnoreCaseAndInitialLeagueAndNameContaining(club: String, initialLeague: String, name: String): PlayerDocument?
    suspend fun findByClubContainingIgnoreCaseAndNameContaining(club: String, name: String): PlayerDocument?
}

@Document(collection = "Players")
data class PlayerDocument(
    @Id
    val id: Long,
    val name: String,
    val club: String,
    val initialRanking: Int,
    val initialLeague: String,
    val promotedToHigherLeagues: Boolean
) {
    fun toPlayer(): Player =
        Player(
            id = id,
            name = name,
            clubName = ClubName(club),
            initialRanking = initialRanking,
            initialLeagueName = LeagueName.valueOf(initialLeague),
            promotedToHigherLeagues = promotedToHigherLeagues
        )
}

internal fun Player.toDocument(): PlayerDocument =
    PlayerDocument(
        id = id,
        name = name,
        club = clubName.value,
        initialRanking = initialRanking,
        initialLeague = initialLeagueName.name,
        promotedToHigherLeagues = promotedToHigherLeagues
    )

@Component
class MongoPlayerRepository(private val repository: JpaPlayerRepository) : PlayerRepository {
    override suspend fun save(player: Player) { repository.save(player.toDocument()) }

    override suspend fun find(criteria: FindPlayerCriteria): Player? =
        withIOContext {
            when (criteria) {
                is ById -> repository.findById(criteria.id)
                is ByClubLeagueAndName ->
                    repository.findByClubContainingIgnoreCaseAndInitialLeagueAndNameContaining(
                        club = criteria.club.value,
                        initialLeague = criteria.leagueName.name,
                        name = criteria.name
                    )
                is ByClubAndName -> repository.findByClubContainingIgnoreCaseAndNameContaining(criteria.club.value, criteria.name)
            }?.toPlayer()
        }

    override suspend fun search(criteria: SearchPlayerCriteria): List<Player> =
        withIOContext {
            when (criteria) {
                is ByClub -> repository.findAllByClub(criteria.club.value)
            }.map { it.toPlayer() }
        }

    override suspend fun exists(criteria: ExistsPlayerCriteria): Boolean =
        withIOContext {
            when (criteria) {
                is ExistsPlayerCriteria.ById -> repository.existsById(criteria.id)
            }
        }
}
