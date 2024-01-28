package com.codely.competition.league.application.search

import com.codely.competition.clubs.domain.ClubName
import com.codely.competition.league.domain.LeagueName
import com.codely.competition.league.domain.League
import com.codely.competition.league.domain.LeagueRepository

context(LeagueRepository)
suspend fun handle(query: SearchLeagueQuery): League? =
    searchLeagueRanking(LeagueName.valueOf(query.league), ClubName(query.club))

data class SearchLeagueQuery(val league: String, val club: String)
