package com.codely.competition.league.application.ranking

import com.codely.competition.clubs.domain.ClubRepository
import com.codely.competition.players.domain.PlayerRepository
import com.codely.competition.league.domain.LeagueName
import com.codely.competition.league.domain.LeagueRepository
import org.springframework.stereotype.Component

@Component
class UpdateRankingCommandHandler(
    playerRepository: PlayerRepository,
    clubRepository: ClubRepository,
    leagueRepository: LeagueRepository
) {

    private val updateRanking = LeagueRankingUpdater(playerRepository, clubRepository, leagueRepository)

    suspend fun handle(command: UpdateLeagueRankingCommand) {
        updateRanking(command.lines, LeagueName.valueOf(command.league))
    }
}

data class UpdateLeagueRankingCommand(val lines: List<String>, val league: String)
