package com.codely.competition.players.domain

import com.codely.competition.clubs.domain.ClubName
import com.codely.competition.league.domain.LeagueName

interface PlayerRepository {

    suspend fun save(player: Player)
    suspend fun find(criteria: FindPlayerCriteria): Player?
    suspend fun search(criteria: SearchPlayerCriteria): List<Player>
    suspend fun exists(criteria: ExistsPlayerCriteria): Boolean
}

sealed interface ExistsPlayerCriteria {
    class ById(val id: Long) : ExistsPlayerCriteria
}

sealed interface FindPlayerCriteria {
    class ById(val id: Long) : FindPlayerCriteria
    class ByClubLeagueAndName(val club: ClubName, val leagueName: LeagueName, val name: String) : FindPlayerCriteria
    class ByClubAndName(val club: ClubName, val name: String) : FindPlayerCriteria
}

sealed interface SearchPlayerCriteria {
    class ByClub(val club: ClubName) : SearchPlayerCriteria
}
