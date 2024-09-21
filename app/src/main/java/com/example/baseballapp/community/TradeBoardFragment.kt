package com.example.baseballapp.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseballapp.ApiObject
import com.example.baseballapp.PagedBoardResponse
import com.example.baseballapp.R
import com.example.baseballapp.databinding.FragmentTradeBoardBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TradeBoardFragment : Fragment() {

    private var _binding: FragmentTradeBoardBinding? = null
    private val binding get() = _binding!!
    private lateinit var postAdapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTradeBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postAdapter = PostAdapter(emptyList()) { post ->
            // 게시글 클릭 시 상세 페이지로 이동
            parentFragmentManager.beginTransaction()
                .replace(R.id.boardContainer, PostDetailFragment.newInstance(post))
                .addToBackStack(null)
                .commit()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = postAdapter

        loadPage(0) // 첫 페이지 로드
    }

    private fun loadPage(page: Int) {
        ApiObject.getRetrofitService.getBoardsByPage("중고거래게시판", page, 1).enqueue(object : Callback<PagedBoardResponse> {
            override fun onResponse(call: Call<PagedBoardResponse>, response: Response<PagedBoardResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { pagedResponse ->
                        postAdapter.setPosts(pagedResponse.content)  // 게시글 설정
                        setupPagination(pagedResponse.totalPages) // 페이지 버튼 생성
                    }
                } else {
                    Toast.makeText(context, "페이지를 불러오는데 실패했습니다. 오류 코드: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PagedBoardResponse>, t: Throwable) {
                Toast.makeText(context, "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupPagination(totalPages: Int) {
        val paginationLayout = binding.paginationLayout
        paginationLayout.removeAllViews() // 기존 버튼 제거

        for (i in 1..totalPages) {
            val textView = TextView(context).apply {
                text = i.toString()
                textSize = 16f  // 글씨 크기 조절
                setPadding(8, 8, 8, 8)  // 패딩 조절
                setTextColor(ContextCompat.getColor(context, R.color.black))
                setOnClickListener {
                    loadPage(i - 1) // 페이지 번호는 0부터 시작하므로 1을 뺌
                }
            }
            paginationLayout.addView(textView)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
