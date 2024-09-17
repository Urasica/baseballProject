package com.example.baseballapp.Ranking

data class PitcherRankData(
    val rank: Int,
    val name: String,
    val team: String,
    val games: Int,
    val wins: Int,
    val losses: Int,
    val save: Int,
    val hold: Int,
    val innings: String,
    val pitchCount: Int,
    val hits: Int,
    val homeRuns: Int,
    val strikeout: Int,
    val baseOnBall: Int,
    val runs: Int,
    val earnedRuns: Int,
    val earnedRunsAVG: Double,
    val whip: Double,
    val qs: Int
)