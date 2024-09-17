package com.example.baseballapp.Ranking

data class HitterRankData(
    val rank: Int,
    val name: String,
    val team: String,
    val games: Int,
    val plateAppearance: Int,
    val atBat: Int,
    val hits: Int,
    val doubles: Int,
    val triples: Int,
    val homeRuns: Int,
    val runBattedIn: Int,
    val runsScored: Int,
    val stolenBases: Int,
    val baseOnBall: Int,
    val strikeOuts: Double,
    val battingAVG: Double,
    val onBaseAVG: Double,
    val sluggingAVG: Double,
    val ops: Double
)
