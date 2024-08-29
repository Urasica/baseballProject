package com.example.baseballapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TeamRankAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var teamList: List<TeamRankData> = emptyList()

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_ITEM = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_HEADER else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.team_rank_header_item, parent, false)
            HeaderViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.team_rank_item, parent, false)
            TeamRankViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TeamRankViewHolder) {
            holder.bind(teamList[position - 1]) // Adjust for header
        }
    }

    override fun getItemCount(): Int = teamList.size + 1 // Add one for the header

    fun setList(newList: List<TeamRankData>) {
        teamList = newList
        notifyDataSetChanged()
    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class TeamRankViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val rank: TextView = itemView.findViewById(R.id.tv_rank)
        private val teamName: TextView = itemView.findViewById(R.id.tv_teamName)
        private val teamLogo: ImageView = itemView.findViewById(R.id.img_team_logo)
        private val games: TextView = itemView.findViewById(R.id.tv_games)
        private val wins: TextView = itemView.findViewById(R.id.tv_wins)
        private val losses: TextView = itemView.findViewById(R.id.tv_losses)
        private val draws: TextView = itemView.findViewById(R.id.tv_draws)
        private val winRate: TextView = itemView.findViewById(R.id.tv_winRate)
        private val winningMargin: TextView = itemView.findViewById(R.id.tv_winningMargin)
        private val continuity: TextView = itemView.findViewById(R.id.tv_continuity)

        fun bind(teamRankData: TeamRankData) {
            rank.text = teamRankData.rank.toString()
            teamName.text = teamRankData.teamName
            games.text = teamRankData.games.toString()
            wins.text = teamRankData.wins.toString()
            losses.text = teamRankData.losses.toString()
            draws.text = teamRankData.draws.toString()
            winRate.text = teamRankData.winRate.toString()
            winningMargin.text = teamRankData.winningMargin.toString()
            continuity.text = teamRankData.continuity

            val logoResource = when (teamRankData.teamName) {
                "두산" -> R.drawable.doosan_logo
                "KIA" -> R.drawable.kia_logo
                "키움" -> R.drawable.kiwoom_logo
                "KT" -> R.drawable.kt_logo
                "LG" -> R.drawable.lg_logo
                "롯데" -> R.drawable.lotte_logo
                "NC" -> R.drawable.nc_logo
                "삼성" -> R.drawable.samsung_logo
                "SSG" -> R.drawable.ssg_logo
                "한화" -> R.drawable.hanwha_logo
                else -> R.drawable.baseball
            }
            teamLogo.setImageResource(logoResource)
        }
    }
}
