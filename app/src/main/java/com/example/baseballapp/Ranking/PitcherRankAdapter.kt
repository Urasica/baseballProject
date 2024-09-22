package com.example.baseballapp.Ranking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.baseballapp.R

class PitcherRankAdapter(
    private var items: List<PitcherRankData>,
    private val registerScrollView: (HorizontalScrollView) -> Unit
) : RecyclerView.Adapter<PitcherRankAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvRank: TextView = view.findViewById(R.id.tv_rank)
        val tvName: TextView = view.findViewById(R.id.tv_name)
        val tvTeam: TextView = view.findViewById(R.id.tv_team)
        val tvGames: TextView = view.findViewById(R.id.tv_games)
        val tvWins: TextView = view.findViewById(R.id.tv_wins)
        val tvLosses: TextView = view.findViewById(R.id.tv_losses)
        val tvSave: TextView = view.findViewById(R.id.tv_save)
        val tvHold: TextView = view.findViewById(R.id.tv_hold)
        val tvInnings: TextView = view.findViewById(R.id.tv_innings)
        val tvPitchCount: TextView = view.findViewById(R.id.tv_pitchCount)
        val tvHits: TextView = view.findViewById(R.id.tv_hits)
        val tvHomeRuns: TextView = view.findViewById(R.id.tv_homeRuns)
        val tvStrikeout: TextView = view.findViewById(R.id.tv_strikeout)
        val tvBaseOnBall: TextView = view.findViewById(R.id.tv_baseOnBall)
        val tvRuns: TextView = view.findViewById(R.id.tv_runs)
        val tvEarnedRuns: TextView = view.findViewById(R.id.tv_earnedRuns)
        val tvEarnedRunsAVG: TextView = view.findViewById(R.id.tv_earnedRunsAVG)
        val tvWhip: TextView = view.findViewById(R.id.tv_whip)
        val tvQs: TextView = view.findViewById(R.id.tv_qs)
        val dataScrollView: HorizontalScrollView = view.findViewById(R.id.data_scroll_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pitcher_rank_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvRank.text = item.rank.toString()
        holder.tvName.text = item.name
        holder.tvTeam.text = item.team
        holder.tvGames.text = item.games.toString()
        holder.tvWins.text = item.wins.toString()
        holder.tvLosses.text = item.losses.toString()
        holder.tvSave.text = item.save.toString()
        holder.tvHold.text = item.hold.toString()
        holder.tvInnings.text = item.innings.toString()
        holder.tvPitchCount.text = item.pitchCount.toString()
        holder.tvHits.text = item.hits.toString()
        holder.tvHomeRuns.text = item.homeRuns.toString()
        holder.tvStrikeout.text = item.strikeout.toString()
        holder.tvBaseOnBall.text = item.baseOnBall.toString()
        holder.tvRuns.text = item.runs.toString()
        holder.tvEarnedRuns.text = item.earnedRuns.toString()
        holder.tvEarnedRunsAVG.text = item.earnedRunsAVG.toString()
        holder.tvWhip.text = item.whip.toString()
        holder.tvQs.text = item.qs.toString()

        // 홀수 순위일 때 배경을 lightgray2로 설정
        if (position % 2 == 0) {
            holder.itemView.setBackgroundResource(R.color.lightgray2)
        } else {
            holder.itemView.setBackgroundResource(android.R.color.white)  // 짝수 순위는 기본 흰색 배경
        }
    }


    override fun getItemCount(): Int {
        return items.size
    }

    fun setList(newItems: List<PitcherRankData>) {
        items = newItems
        notifyDataSetChanged()
    }
}
