package com.example.baseballapp.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.baseballapp.ApiObject
import com.example.baseballapp.Post
import com.example.baseballapp.databinding.FragmentWritePostBinding
import com.example.login.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class WritePostFragment : Fragment() {

    private var _binding: FragmentWritePostBinding? = null
    private val binding get() = _binding!!

    private lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWritePostBinding.inflate(inflater, container, false)
        tokenManager = TokenManager(requireContext()) // TokenManager 초기화
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 저장된 토큰을 ApiObject에 설정
        val token = tokenManager.getToken()
        if (token != null) {
            ApiObject.setToken(token)
        } else {
            Toast.makeText(context, "토큰이 존재하지 않습니다. 로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
        }

        binding.submitPostButton.setOnClickListener {
            // 작성자 이름을 TokenManager에서 가져옴
            val author = tokenManager.getUsername() ?: "알 수 없는 사용자"
            val title = binding.postTitle.text.toString()
            val content = binding.postContent.text.toString()
            val createdAt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(Date())
            val updatedAt = createdAt
            val selectedBoardType = binding.boardSpinner.selectedItem.toString()

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(context, "제목과 내용을 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val post = Post(title, content, author, createdAt, updatedAt, selectedBoardType)
            submitPost(post)
        }
    }

    private fun submitPost(post: Post) {
        ApiObject.getRetrofitService.submitPost(post).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "게시글이 성공적으로 작성되었습니다.", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                } else {
                    Toast.makeText(context, "게시글 작성에 실패했습니다. 오류 코드: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context, "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
