package com.example.baseballapp.community

import android.graphics.Paint
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
    private var currentPage = 0  // 현재 페이지를 기억하는 변수
    private fun setupPagination(totalPages: Int) {
        val paginationLayout = binding.paginationLayout
        paginationLayout.removeAllViews() // 기존 버튼 제거

        var selectedTextView: TextView? = null  // 선택된 페이지 번호를 저장할 변수

        for (i in 1..totalPages) {
            val textView = TextView(requireContext()).apply {
                text = i.toString()
                textSize = if (i - 1 == currentPage) 23f else 18f  // 현재 페이지는 크게, 나머지는 기본 크기
                setPadding(8, 8, 8, 8)  // 패딩 조절
                setTextColor(
                    if (i - 1 == currentPage) ContextCompat.getColor(requireContext(), R.color.navy)
                    else ContextCompat.getColor(requireContext(), R.color.black)
                )  // 현재 페이지는 네이비, 나머지는 기본 색상
                paintFlags = if (i - 1 == currentPage) paintFlags or Paint.UNDERLINE_TEXT_FLAG
                else paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()  // 현재 페이지는 밑줄, 나머지는 밑줄 제거

                setOnClickListener {
                    loadPage(i - 1)  // 페이지 로드

                    // 선택된 페이지 번호 스타일 변경
                    selectedTextView?.apply {
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                        textSize = 18f  // 기본 크기
                        paintFlags = paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()  // 밑줄 제거
                    }

                    // 클릭된 페이지 번호 스타일을 업데이트
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.navy))  // 네이비 색상
                    textSize = 23f  // 글씨 크기 크게
                    paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG  // 밑줄 추가

                    // 현재 선택된 페이지 번호를 저장
                    selectedTextView = this

                    // 현재 페이지 업데이트
                    currentPage = i - 1
                }
            }

            // 현재 페이지 번호를 기본적으로 선택된 상태로 설정
            if (i - 1 == currentPage) {
                selectedTextView = textView
            }

            paginationLayout.addView(textView)
        }
    }
    private fun loadPage(page: Int) {
        currentPage = page  // 페이지 로드 시 currentPage 업데이트
        ApiObject.getRetrofitService.getBoardsByPage("중고거래게시판", page, 1).enqueue(object : Callback<PagedBoardResponse> {
            override fun onResponse(call: Call<PagedBoardResponse>, response: Response<PagedBoardResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { pagedResponse ->
                        postAdapter.setPosts(pagedResponse.content)  // 게시글 로드
                        setupPagination(pagedResponse.totalPages)  // 페이지 번호 업데이트
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
