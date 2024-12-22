package com.codely.competition.league.application.search

import com.codely.competition.clubs.domain.ClubName
import com.codely.competition.league.domain.LeagueName
import com.codely.competition.league.domain.League
import com.codely.competition.league.domain.LeagueRepository
import com.codely.competition.league.domain.SearchLeagueCriteria

context(LeagueRepository)
suspend fun handle(query: SearchLeagueQuery): League? {
    val leagueName = LeagueName.valueOf(query.league)
    val clubName = query.club?.let { ClubName(it) }
    return searchLeagueRanking(SearchLeagueCriteria.from(leagueName, clubName))
}

data class SearchLeagueQuery(val league: String, val club: String?)
