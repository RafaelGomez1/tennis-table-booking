package com.codely.competition.clubs.domain

import com.codely.competition.league.domain.LeagueName
import java.util.UUID

data class Club(
    val clubName: ClubName,
    val leagueName: LeagueName,
    val id: UUID = UUID.randomUUID()
)

@JvmInline
value class ClubName(val value: String)
