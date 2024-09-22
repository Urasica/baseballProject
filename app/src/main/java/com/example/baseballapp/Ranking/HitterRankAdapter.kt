package com.example.baseballapp.Ranking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.baseballapp.R

class HitterRankAdapter(
    private var hitterList: List<HitterRankData> = emptyList(),
    private val registerScrollView: (HorizontalScrollView) -> Unit
) : RecyclerView.Adapter<HitterRankAdapter.HitterRankViewHolder>() {

    inner class HitterRankViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val rank: TextView = itemView.findViewById(R.id.tv_rank)
        private val name: TextView = itemView.findViewById(R.id.tv_name)
        private val team: TextView = itemView.findViewById(R.id.tv_team)
        private val games: TextView = itemView.findViewById(R.id.tv_games)
        private val plateAppearance: TextView = itemView.findViewById(R.id.tv_plateAppearance)
        private val atBat: TextView = itemView.findViewById(R.id.tv_atBat)
        private val hits: TextView = itemView.findViewById(R.id.tv_hits)
        private val doubles: TextView = itemView.findViewById(R.id.tv_doubles)
        private val triples: TextView = itemView.findViewById(R.id.tv_triples)
        private val homeRuns: TextView = itemView.findViewById(R.id.tv_homeRuns)
        private val runBattedln: TextView = itemView.findViewById(R.id.tv_runBattedln)
        private val runScored: TextView = itemView.findViewById(R.id.tv_runScored)
        private val stolenBases: TextView = itemView.findViewById(R.id.tv_stolenBases)
        private val baseOnBall: TextView = itemView.findViewById(R.id.tv_baseOnBall)
        private val strikeOuts: TextView = itemView.findViewById(R.id.tv_strikeOuts)
        private val battingAVG: TextView = itemView.findViewById(R.id.tv_battingAVG)
        private val onBaseAVG: TextView = itemView.findViewById(R.id.tv_onBaseAVG)
        private val sluggingAVG: TextView = itemView.findViewById(R.id.tv_sluggingAVG)
        private val ops: TextView = itemView.findViewById(R.id.tv_ops)
        private val dataScrollView: HorizontalScrollView = itemView.findViewById(R.id.data_scroll_view)

        fun bind(hitterRankData: HitterRankData) {
            rank.text = hitterRankData.rank.toString()
            name.text = hitterRankData.name
            team.text = hitterRankData.team
            games.text = hitterRankData.games.toString()
            plateAppearance.text = hitterRankData.plateAppearance.toString()
            atBat.text = hitterRankData.atBat.toString()
            hits.text = hitterRankData.hits.toString()
            doubles.text = hitterRankData.doubles.toString()
            triples.text = hitterRankData.triples.toString()
            homeRuns.text = hitterRankData.homeRuns.toString()
            runBattedln.text = hitterRankData.runBattedIn.toString()
            runScored.text = hitterRankData.runsScored.toString()
            stolenBases.text = hitterRankData.stolenBases.toString()
            baseOnBall.text = hitterRankData.baseOnBall.toString()
            strikeOuts.text = hitterRankData.strikeOuts.toString()
            battingAVG.text = hitterRankData.battingAVG.toString()
            onBaseAVG.text = hitterRankData.onBaseAVG.toString()
            sluggingAVG.text = hitterRankData.sluggingAVG.toString()
            ops.text = hitterRankData.ops.toString()


            registerScrollView(dataScrollView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HitterRankViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.hitter_rank_item, parent, false)
        return HitterRankViewHolder(view)
    }

    override fun onBindViewHolder(holder: HitterRankViewHolder, position: Int) {
        holder.bind(hitterList[position])

        // 홀수 순위일 때 배경을 lightgray2로 설정
        if (position % 2 == 0) {
            holder.itemView.setBackgroundResource(R.color.lightgray2)
        } else {
            holder.itemView.setBackgroundResource(android.R.color.white)  // 짝수 순위는 기본 흰색 배경
        }
    }


    override fun getItemCount(): Int = hitterList.size

    fun setList(newList: List<HitterRankData>) {
        hitterList = newList
        notifyDataSetChanged()
    }
}