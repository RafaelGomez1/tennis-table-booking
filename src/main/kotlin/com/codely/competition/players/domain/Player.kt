package com.codely.competition.players.domain

import com.codely.competition.clubs.domain.ClubName
import com.codely.competition.league.domain.LeagueName

data class Player(
    val id: PlayerId,
    val name: String,
    val clubName: ClubName,
    val initialRanking: Ranking,
    val initialLeagueName: LeagueName,
    val promotedToHigherLeagues: Boolean
) {
    companion object {
        fun create(
            id: PlayerId,
            name: String,
            club: String,
            initialRanking: Int,
            leagueName: LeagueName,
            promotedToHigherLeagues: Boolean
        ) = Player(id, name, ClubName(club), initialRanking, leagueName, promotedToHigherLeagues)
    }
}

typealias PlayerId = Long
typealias Ranking = Int
