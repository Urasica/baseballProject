package com.example.baseballapp.Ranking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.baseballapp.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RankingFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var teamRankAdapter: TeamRankAdapter
    private lateinit var hitterRankAdapter: HitterRankAdapter
    private lateinit var pitcherRankAdapter: PitcherRankAdapter
    private lateinit var headerScrollView: HorizontalScrollView
    private lateinit var rootView: View
    private val dataScrollViews = mutableListOf<HorizontalScrollView>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_ranking, container, false)

        // View 초기화
        recyclerView = rootView.findViewById(R.id.recycler_view_rankings)
        headerScrollView = rootView.findViewById(R.id.header_scroll_view)

        teamRankAdapter = TeamRankAdapter()
        hitterRankAdapter = HitterRankAdapter(emptyList(), ::registerScrollView)
        pitcherRankAdapter = PitcherRankAdapter(emptyList(), ::registerScrollView)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = teamRankAdapter

        // Spinner 설정
        val spinner = rootView.findViewById<Spinner>(R.id.spinner_ranking_category)
        val categories = resources.getStringArray(R.array.ranking_categories)

        // 커스텀 ArrayAdapter 생성
        val adapter = ArrayAdapter(requireContext(), R.layout.custom_spinner_item, categories)
        adapter.setDropDownViewResource(R.layout.custom_spinner_item)
        spinner.adapter = adapter

        // Spinner 아이템 선택 리스너
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedCategory = parent.getItemAtPosition(position).toString()
                when (selectedCategory) {
                    "팀순위" -> showTeamRankings()
                    "타자 주요순위" -> showHitterRankings()
                    "투수 주요순위" -> showPitcherRankings()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // ScrollView 동기화
        headerScrollView.setOnScrollChangeListener { _, scrollX, _, _, _ ->
            dataScrollViews.forEach { it.scrollTo(scrollX, 0) }
        }

        return rootView
    }

    private fun showTeamRankings() {
        headerScrollView.visibility = View.GONE
        recyclerView.adapter = teamRankAdapter
        fetchTeamRankings()
    }

    private fun showHitterRankings() {
        headerScrollView.visibility = View.VISIBLE
        headerScrollView.removeAllViews()
        val hitterHeaderView = layoutInflater.inflate(R.layout.hitter_rank_header_item, headerScrollView, false)
        headerScrollView.addView(hitterHeaderView)
        recyclerView.adapter = hitterRankAdapter
        fetchHitterRankings()
    }

    private fun showPitcherRankings() {
        headerScrollView.visibility = View.VISIBLE
        headerScrollView.removeAllViews()
        val pitcherHeaderView = layoutInflater.inflate(R.layout.pitcher_rank_header_item, headerScrollView, false)
        headerScrollView.addView(pitcherHeaderView)
        recyclerView.adapter = pitcherRankAdapter
        fetchPitcherRankings()
    }

    private fun fetchTeamRankings() {
        ApiObject.getRetrofitService.getAllTeams().enqueue(object : Callback<List<TeamRankData>> {
            override fun onResponse(call: Call<List<TeamRankData>>, response: Response<List<TeamRankData>>) {
                if (response.isSuccessful) {
                    response.body()?.let { teamList ->
                        teamRankAdapter.setList(teamList)

                        val logoFirst = rootView.findViewById<ImageView>(R.id.logo_first)
                        val logoSecond = rootView.findViewById<ImageView>(R.id.logo_second)
                        val logoThird = rootView.findViewById<ImageView>(R.id.logo_third)
                        val teamFirst = rootView.findViewById<TextView>(R.id.team_first)
                        val teamSecond = rootView.findViewById<TextView>(R.id.team_second)
                        val teamThird = rootView.findViewById<TextView>(R.id.team_third)

                        logoFirst.setImageResource(getTeamLogoResource(teamList[0].teamName))
                        teamFirst.text = "${teamList[0].teamName} (1위)"
                        logoSecond.setImageResource(getTeamLogoResource(teamList[1].teamName))
                        teamSecond.text = "${teamList[1].teamName} (2위)"
                        logoThird.setImageResource(getTeamLogoResource(teamList[2].teamName))
                        teamThird.text = "${teamList[2].teamName} (3위)"
                    }
                }
            }

            override fun onFailure(call: Call<List<TeamRankData>>, t: Throwable) {
                Toast.makeText(context, "데이터를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchHitterRankings() {
        ApiObject.getRetrofitService.getAllHitters().enqueue(object : Callback<List<HitterRankData>> {
            override fun onResponse(call: Call<List<HitterRankData>>, response: Response<List<HitterRankData>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        hitterRankAdapter.setList(it)
                    }
                }
            }

            override fun onFailure(call: Call<List<HitterRankData>>, t: Throwable) {
                Toast.makeText(context, "데이터를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchPitcherRankings() {
        ApiObject.getRetrofitService.getAllPitchers().enqueue(object : Callback<List<PitcherRankData>> {
            override fun onResponse(call: Call<List<PitcherRankData>>, response: Response<List<PitcherRankData>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        pitcherRankAdapter.setList(it)
                    }
                }
            }

            override fun onFailure(call: Call<List<PitcherRankData>>, t: Throwable) {
                Toast.makeText(context, "데이터를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getTeamLogoResource(teamName: String): Int {
        return when (teamName) {
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
    }

    private fun registerScrollView(scrollView: HorizontalScrollView) {
        dataScrollViews.add(scrollView)
    }
}
