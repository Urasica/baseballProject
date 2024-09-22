import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

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

        // 웹소켓 설정 및 연결
        setupWebSocket(roomId)

        chatAdapter = ChatAdapter(messageList)
        binding.recyclerViewMessages.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewMessages.adapter = chatAdapter

        binding.buttonSend.setOnClickListener {
            val message = binding.editTextMessage.text.toString()
            if (message.isNotEmpty()) {
                val time = getCurrentTimeInKorea()
                val formattedMessage = "$nickname  $time\n$message"
                webSocket.send(formattedMessage)
                binding.editTextMessage.text.clear()
            }
        }

        // 팀 정보와 스코어 데이터 가져오기
        val team1Name = arguments?.getString("TEAM1_NAME") ?: "팀1"
        val team2Name = arguments?.getString("TEAM2_NAME") ?: "팀2"
        val team1Score = arguments?.getString("TEAM1_SCORE") ?: "0"
        val team2Score = arguments?.getString("TEAM2_SCORE") ?: "0"

        // UI에 팀 정보와 스코어 설정
        setTeamInfo(team1Name, team2Name, team1Score, team2Score)

        val teamName = arguments?.getString("TEAM_NAME")
        val matchDate = arguments?.getString("MATCH_DATE")

        if (teamName != null && matchDate != null) {
            fetchMatchDetails(teamName, matchDate)
        }

        setInningButtonListeners()
    }

    // 웹소켓 설정 메서드
    private fun setupWebSocket(roomId: String) {
        val client = OkHttpClient()
        val request = Request.Builder().url("ws://35.216.0.159:8080/ws/chat/$roomId").build()
        val listener = ChatWebSocketListener(this)
        webSocket = client.newWebSocket(request, listener)
    }

    // 추가: 팀 정보 설정 메서드
    private fun setTeamInfo(team1Name: String, team2Name: String, team1Score: String, team2Score: String) {
        binding.team1Name.text = team1Name
        binding.team2Name.text = team2Name
        binding.team1Score.text = team1Score
        binding.team2Score.text = team2Score

        // 로고 설정 (팀 이름에 따라 로고 리소스를 설정, 예시로 팀 이름으로 매핑)
        val team1Logo = getLogoResource(team1Name)
        val team2Logo = getLogoResource(team2Name)
        binding.team1Logo.setImageResource(team1Logo)
        binding.team2Logo.setImageResource(team2Logo)
    }

    // 팀 이름에 따라 로고 리소스를 매핑하는 메서드 예시
    private fun getLogoResource(teamName: String): Int {
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
            else -> R.drawable.baseball // 기본 로고 (팀 이름이 일치하지 않을 경우)
        }
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
        val stringBuilder = SpannableStringBuilder()

        inning?.details?.forEach { detail ->
            val detailParts = detail.split("|")

            detailParts.forEachIndexed { index, part ->
                if (index == 0 && part.contains("공격")) {

                    val spannableString = SpannableString(part.trim() + "\n")
                    spannableString.setSpan(
                        StyleSpan(Typeface.BOLD),
                        0, spannableString.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    spannableString.setSpan(
                        RelativeSizeSpan(1.2f),
                        0, spannableString.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    stringBuilder.append(spannableString)
                } else if (part.contains("타자")) {
                    // 타자 문구를 빨간색으로 볼드체로 설정
                    val spannableString = SpannableString(part.trim() + "\n")
                    spannableString.setSpan(
                        ForegroundColorSpan(Color.RED),
                        0, spannableString.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    spannableString.setSpan(
                        StyleSpan(Typeface.BOLD),
                        0, spannableString.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    stringBuilder.append(spannableString)
                    stringBuilder.append("\n")
                } else if (part.contains(":")) {
                    // 결과 문구를  darkblue로 설정
                    val darkBlueColor = ContextCompat.getColor(requireContext(), R.color.darkblue)
                    val resultStartIndex = part.indexOf(":") + 1
                    val resultText = part.substring(resultStartIndex).trim()
                    if (resultText.isNotEmpty()) {
                        val spannableString = SpannableString(part.trim() + "\n")
                        spannableString.setSpan(
                            ForegroundColorSpan(darkBlueColor),
                            resultStartIndex, spannableString.length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        stringBuilder.append(spannableString)
                    } else {
                        stringBuilder.append(part.trim() + "\n")
                    }
                } else {
                    stringBuilder.append(part.trim() + "\n")
                }
            }
        }

        binding.liveStreamTextView.text = stringBuilder
        binding.liveStreamTextView.visibility = View.VISIBLE
    }

    private fun getCurrentTimeInKorea(): String {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.KOREAN).apply {
            timeZone = TimeZone.getTimeZone("Asia/Seoul")
        }
        return dateFormat.format(Date())
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
