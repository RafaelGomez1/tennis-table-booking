package com.codely.competition.clubs.application.search

import com.codely.competition.clubs.domain.Club
import com.codely.competition.clubs.domain.ClubRepository
import com.codely.competition.clubs.domain.SearchClubCriteria.ByLeague
import com.codely.competition.league.domain.LeagueName

context(ClubRepository)
suspend fun searchClub(leagueName: LeagueName): List<Club> = search(ByLeague(leagueName))
