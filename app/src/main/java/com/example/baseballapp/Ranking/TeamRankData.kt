package com.example.baseballapp.Ranking

data class TeamRankData(
    val rank: Int,
    val teamName: String,
    val games: Int,
    val wins: Int,
    val losses: Int,
    val draws: Int,
    val winRate: Double,
    val winningMargin: Double,
    val continuity: String
)
