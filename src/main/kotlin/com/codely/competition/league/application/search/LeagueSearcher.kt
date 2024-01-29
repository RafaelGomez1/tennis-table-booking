package com.codely.competition.league.application.search

import com.codely.competition.league.domain.League
import com.codely.competition.league.domain.LeagueRepository
import com.codely.competition.league.domain.SearchLeagueCriteria

context(LeagueRepository)
suspend fun searchLeagueRanking(criteria: SearchLeagueCriteria): League? = search(criteria)
