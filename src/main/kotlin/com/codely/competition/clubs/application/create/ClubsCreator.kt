package com.codely.competition.clubs.application.create

import com.codely.competition.clubs.domain.Club
import com.codely.competition.clubs.domain.ClubExistsCriteria.ByNameAndLeague
import com.codely.competition.clubs.domain.ClubRepository
import com.codely.competition.clubs.domain.ClubName
import com.codely.competition.league.domain.LeagueName
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class ClubsCreator(private val repository: ClubRepository) {

    suspend operator fun invoke(clubNames: List<ClubName>, leagueName: LeagueName) = coroutineScope {
        clubNames
            .forEach { clubName ->
                launch {
                    if (repository.exists(ByNameAndLeague(clubName, leagueName))) Unit.also { println("Club $clubName already exists in $leagueName")  }
                    else repository.save(Club(clubName, leagueName)).also { println("Persisting $clubName in $leagueName")  }
                }.join()
            }
    }
}
