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
import com.example.baseballapp.R
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
    private lateinit var matchResponse: MatchResponse

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val roomId = arguments?.getString("ROOM_ID") ?: "default"
        nickname = arguments?.getString("NICKNAME") ?: "sumin"

        Log.d("ChatFragment", "Room ID: $roomId")
        Log.d("ChatFragment", "Nickname: $nickname")

        val client = OkHttpClient()
        val request = Request.Builder().url("ws://35.216.0.159:8080/ws/chat/$roomId").build()
        val listener = ChatWebSocketListener(this)
        webSocket = client.newWebSocket(request, listener)

        chatAdapter = ChatAdapter(messageList)

        binding.recyclerViewMessages.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewMessages.adapter = chatAdapter

        binding.buttonSend.setOnClickListener {
            val message = binding.editTextMessage.text.toString()
            if (message.isNotEmpty()) {
                val formattedMessage = "$nickname\n$message"
                webSocket.send(formattedMessage)
                binding.editTextMessage.text.clear()
            }
        }

        val teamName = arguments?.getString("TEAM_NAME")
        val matchDate = arguments?.getString("MATCH_DATE")

        if (teamName != null && matchDate != null) {
            fetchMatchDetails(teamName, matchDate)
        }

        // 이닝 버튼 클릭 리스너 설정
        setInningButtonListeners()
    }

    private fun fetchMatchDetails(teamName: String, matchDate: String) {
        ApiObject.getRetrofitService.getMatchDetails(teamName, matchDate).enqueue(object : Callback<MatchResponse> {
            override fun onResponse(call: Call<MatchResponse>, response: Response<MatchResponse>) {
                if (response.isSuccessful) {
                    matchResponse = response.body()!!
                    displayMatchDetails(matchResponse.innings.firstOrNull()?.inningNumber ?: 1)
                } else {
                    binding.liveStreamTextView.text = "문자 중계 데이터를 불러오지 못했습니다."
                }
            }

            override fun onFailure(call: Call<MatchResponse>, t: Throwable) {
                binding.liveStreamTextView.text = "문자 중계 데이터를 불러오지 못했습니다."
            }
        })
    }

    private fun setInningButtonListeners() {
        val buttons = listOf(
            binding.buttonInning1,
            binding.buttonInning2,
            binding.buttonInning3,
            binding.buttonInning4,
            binding.buttonInning5,
            binding.buttonInning6,
            binding.buttonInning7,
            binding.buttonInning8,
            binding.buttonInning9
        )

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                displayMatchDetails(index + 1)
            }
        }
    }

    private fun displayMatchDetails(inningNumber: Int) {
        val inning = matchResponse.innings.find { it.inningNumber == inningNumber }
        val stringBuilder = StringBuilder()

        inning?.details?.forEach { detail ->
            stringBuilder.append("- $detail\n")
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
