package com.example.baseballapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.baseballapp.ApiObject.getRetrofitService
import com.example.baseballapp.databinding.FragmentScheduleBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class ScheduleFragment : Fragment() {
    private lateinit var binding: FragmentScheduleBinding
    private lateinit var schedule: TextView
    private lateinit var gameListAdapter: GameListAdapter
    private var gameList = ArrayList<GameListData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDate()

        binding.calendar.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame, CalendarFragment())
                .commitAllowingStateLoss()
        }

        gameListAdapter = GameListAdapter(gameList)
        binding.gamelist.adapter = gameListAdapter

        gameListAdapter.notifyDataSetChanged()

        // 경기 선택 시 처리
        binding.gamelist.setOnItemClickListener { parent, view, position, id ->
            val game = gameList[position]
            val roomId = game.roomId
            val nickname = "soo_ob"

            // 선택한 경기의 팀 이름과 날짜를 가져와 ChatingFragment에 전달
            val teamName = game.team1  // team1을 기준으로 합니다.
            val matchDate = game.date

            val fragment = ChatingFragment().apply {
                arguments = Bundle().apply {
                    putString("ROOM_ID", roomId)
                    putString("NICKNAME", nickname)
                    putString("TEAM_NAME", teamName)
                    putString("MATCH_DATE", matchDate)
                }
            }

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame, fragment)
                .commitAllowingStateLoss()
        }

        binding.switch1.setOnCheckedChangeListener { CompoundButton, onSwitch ->
            if (onSwitch) {
                val intent = Intent(requireContext(), Metaverse1Activity::class.java)
                startActivity(intent)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupDate() {
        schedule = binding.schedule

        var currentDate = LocalDate.now()

        fun updateDate(date: LocalDate) {
            // 오늘 날짜
            val dateFormat = DateTimeFormatter.ofPattern("MM.dd(E)", Locale.KOREAN)
            val formattedDate = date.format(dateFormat)

            schedule.text = formattedDate

            fetchSchedule(formattedDate)
        }

        updateDate(currentDate)

        binding.left.setOnClickListener { // 전날로 이동
            currentDate = currentDate.minusDays(1)
            updateDate(currentDate)
        }

        binding.right.setOnClickListener { // 다음날로 이동
            currentDate = currentDate.plusDays(1)
            updateDate(currentDate)
        }
    }

    private fun fetchSchedule(selectedDate: String) {
        val call = getRetrofitService.getAllSchedule()
        call.enqueue(object : Callback<List<GameListData>> {
            override fun onResponse(
                call: Call<List<GameListData>>,
                response: Response<List<GameListData>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val filterGame = it.filter { game -> game.date == selectedDate }
                        gameList.clear()
                        gameList.addAll(filterGame)
                        if (selectedDate.endsWith("(월)") || filterGame.isEmpty()) {
                            gameList.add(GameListData("", "", "", "", "", "", "오늘은 경기가 없습니다.", "", ""))
                        }
                        gameListAdapter.notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<GameListData>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
