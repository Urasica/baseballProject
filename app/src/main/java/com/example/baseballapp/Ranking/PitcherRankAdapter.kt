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

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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

        fun bind(pitcherRankData: PitcherRankData) {
            tvRank.text = pitcherRankData.rank.toString()
            tvName.text = pitcherRankData.name
            tvTeam.text = pitcherRankData.team
            tvGames.text = pitcherRankData.games.toString()
            tvWins.text = pitcherRankData.wins.toString()
            tvLosses.text = pitcherRankData.losses.toString()
            tvSave.text = pitcherRankData.save.toString()
            tvHold.text = pitcherRankData.hold.toString()
            tvInnings.text = pitcherRankData.innings.toString()
            tvPitchCount.text = pitcherRankData.pitchCount.toString()
            tvHits.text = pitcherRankData.hits.toString()
            tvHomeRuns.text = pitcherRankData.homeRuns.toString()
            tvStrikeout.text = pitcherRankData.strikeout.toString()
            tvBaseOnBall.text = pitcherRankData.baseOnBall.toString()
            tvRuns.text = pitcherRankData.runs.toString()
            tvEarnedRuns.text = pitcherRankData.earnedRuns.toString()
            tvEarnedRunsAVG.text = pitcherRankData.earnedRunsAVG.toString()
            tvWhip.text = pitcherRankData.whip.toString()
            tvQs.text = pitcherRankData.qs.toString()

            // 스크롤뷰를 등록하여 스크롤 이벤트 동기화
            registerScrollView(dataScrollView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pitcher_rank_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])

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
