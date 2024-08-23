package com.example.baseballapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseballapp.ApiObject
import com.example.baseballapp.ChatAdapter
import com.example.baseballapp.ChatMessageData
import com.example.baseballapp.ChatWebSocketListener
import com.example.baseballapp.MatchResponse
import com.example.baseballapp.databinding.FragmentChatingBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatingFragment : Fragment() {
    private lateinit var webSocket: WebSocket
    private lateinit var binding: FragmentChatingBinding
    private lateinit var nickname: String
    private val messageList = ArrayList<ChatMessageData>()
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 채팅 방 ID와 사용자 닉네임을 가져옴
        val roomId = arguments?.getString("ROOM_ID") ?: "default"
        nickname = arguments?.getString("NICKNAME") ?: "sumin"

        Log.d("ChatFragment", "Room ID: $roomId")
        Log.d("ChatFragment", "Nickname: $nickname")

        // 웹소켓 연결 설정
        val client = OkHttpClient()
        val request = Request.Builder().url("ws://35.216.0.159:8080/ws/chat/$roomId").build()
        val listener = ChatWebSocketListener(this)
        webSocket = client.newWebSocket(request, listener)

        chatAdapter = ChatAdapter(messageList)

        // 리사이클러뷰 설정
        binding.recyclerViewMessages.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewMessages.adapter = chatAdapter

        // 보내기 버튼 클릭 리스너 설정
        binding.buttonSend.setOnClickListener {
            val message = binding.editTextMessage.text.toString()
            if (message.isNotEmpty()) {
                val formattedMessage = "$nickname\n$message"
                webSocket.send(formattedMessage)
                binding.editTextMessage.text.clear()
            }
        }

        // 경기 정보가 제대로 전달되었는지 확인하고 API 호출
        val teamName = arguments?.getString("TEAM_NAME")
        val matchDate = arguments?.getString("MATCH_DATE")

        Log.d("ChatingFragment", "Received TEAM_NAME: $teamName")
        Log.d("ChatingFragment", "Received MATCH_DATE: $matchDate")

        if (teamName != null && matchDate != null) {
            fetchMatchDetails(teamName, matchDate)
        } else {
            Log.e("ChatingFragment", "팀 이름이나 날짜가 전달되지 않았습니다.")
            binding.liveStreamTextView.text = "팀 이름이나 날짜가 전달되지 않았습니다."
        }
    }

    private fun fetchMatchDetails(teamName: String, matchDate: String) {
        ApiObject.getRetrofitService.getMatchDetails(teamName, matchDate).enqueue(object : Callback<MatchResponse> {
            override fun onResponse(call: Call<MatchResponse>, response: Response<MatchResponse>) {
                if (response.isSuccessful) {
                    val matchResponse = response.body()
                    matchResponse?.let {
                        if (it.innings.isNotEmpty()) {
                            displayMatchDetails(it)
                        } else {
                            // 만약 문자 중계 데이터가 없으면 TextView를 숨기고 채팅만 보여줌
                            binding.liveStreamTextView.visibility = View.GONE
                        }
                    }
                } else {
                    binding.liveStreamTextView.text = "문자 중계 데이터를 불러오지 못했습니다."
                }
            }

            override fun onFailure(call: Call<MatchResponse>, t: Throwable) {
                binding.liveStreamTextView.text = "문자 중계 데이터를 불러오지 못했습니다."
            }
        })
    }

    private fun displayMatchDetails(matchResponse: MatchResponse) {
        val stringBuilder = StringBuilder()
        for (inning in matchResponse.innings) {
            stringBuilder.append("이닝 ${inning.inningNumber}:\n")
            for (detail in inning.details) {
                stringBuilder.append("- $detail\n")
            }
            stringBuilder.append("\n")
        }
        binding.liveStreamTextView.text = stringBuilder.toString()
        binding.liveStreamTextView.visibility = View.VISIBLE
    }

    fun showMessage(message: String) {
        val chatMessage = ChatMessageData(R.drawable.baseball, message)
        addMessage(chatMessage)
    }

    private fun addMessage(chatMessage: ChatMessageData) {
        messageList.add(chatMessage)
        chatAdapter.notifyDataSetChanged()
        binding.recyclerViewMessages.scrollToPosition(messageList.size - 1)
    }
}
