package com.codely.competition.league.application.search

import com.codely.competition.clubs.domain.ClubName
import com.codely.competition.league.domain.LeagueName
import com.codely.competition.league.domain.League
import com.codely.competition.league.domain.LeagueRepository
import com.codely.competition.league.domain.SearchLeagueCriteria.ByNameAndClub

context(LeagueRepository)
suspend fun searchLeagueRanking(leagueName: LeagueName, club: ClubName): League? =
    search(ByNameAndClub(leagueName, club))
