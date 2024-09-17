package com.example.baseballapp.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
    private var currentPage = 0
    private val pageSize = 10

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
            parentFragmentManager.beginTransaction()
                .replace(R.id.boardContainer, PostDetailFragment.newInstance(post))
                .addToBackStack(null)
                .commit()
        }
        binding.recyclerView.adapter = postAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        fetchPosts()
    }

    private fun fetchPosts() {
        ApiObject.getRetrofitService.getBoardsByPage("중고거래게시판", currentPage, pageSize).enqueue(object : Callback<PagedBoardResponse> {
            override fun onResponse(call: Call<PagedBoardResponse>, response: Response<PagedBoardResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { pagedResponse ->
                        postAdapter.setPosts(pagedResponse.content)
                        currentPage++
                    }
                } else {
                    Toast.makeText(context, "게시글을 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PagedBoardResponse>, t: Throwable) {
                Toast.makeText(context, "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun performSearch(query: String) {
        ApiObject.getRetrofitService.searchBoards(query, "중고거래게시판", 0).enqueue(object : Callback<PagedBoardResponse> {
            override fun onResponse(call: Call<PagedBoardResponse>, response: Response<PagedBoardResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { pagedResponse ->
                        postAdapter.setPosts(pagedResponse.content)
                    }
                } else {
                    Toast.makeText(context, "검색 결과를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
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
