package com.codely.competition.league.domain

enum class LeagueGroup {
    G1, G2, G3, G4, G5, G6;

    companion object {
        fun fromString(value: String): LeagueGroup =
            values().first { it.name == value.uppercase() }
    }
}

enum class LeagueName(val value: String) {
    NACIONAL("NAC"),
    LOQUESEA("TDM"),
    PREFERENT("PREF"),
    PRIMERA("1a"),
    SEGUNDA_A("2aA"),
    SEGUNDA_B("2aB"),
    TERCERA_A("3aA"),
    TERCERA_B("3aB"),
    CUARTA("4a");

    companion object {
        fun parseNames(): List<String> = values().map { it.value }
    }
}

@JvmInline
value class Standing(val value: Int)

@JvmInline
value class Points(val value: Int)
