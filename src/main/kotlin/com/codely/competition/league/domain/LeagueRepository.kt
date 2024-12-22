package com.codely.competition.league.domain

import com.codely.competition.clubs.domain.ClubName

interface LeagueRepository {
    suspend fun save(league: League)
    suspend fun delete(leagueName: LeagueName)
    suspend fun search(criteria: SearchLeagueCriteria): League?
}

sealed interface SearchLeagueCriteria {
    class ByNameAndClub(val leagueName: LeagueName, val clubName: ClubName) : SearchLeagueCriteria
    class ByName(val leagueName: LeagueName) : SearchLeagueCriteria

    companion object {
        fun from(leagueName: LeagueName, clubName: ClubName?): SearchLeagueCriteria =
            when {
                clubName == null -> ByName(leagueName)
                else -> ByNameAndClub(leagueName, clubName)
            }
    }
}
