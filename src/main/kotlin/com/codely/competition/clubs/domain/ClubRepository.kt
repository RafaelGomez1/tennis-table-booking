package com.codely.competition.clubs.domain

import com.codely.competition.league.domain.LeagueName

interface ClubRepository {
    suspend fun save(club: Club)
    suspend fun search(criteria: SearchClubCriteria): List<Club>
    suspend fun exists(criteria: ClubExistsCriteria): Boolean
}

sealed interface ClubExistsCriteria {
    class ByNameAndLeague(val clubName: ClubName, val leagueName: LeagueName) : ClubExistsCriteria
}

sealed interface SearchClubCriteria {
    data object All : SearchClubCriteria
    class ByLeague(val leagueName: LeagueName) : SearchClubCriteria
}
