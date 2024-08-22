package com.example.baseballapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseballapp.databinding.FragmentCommunityBinding
import com.example.yourapp.WritePostFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommunityFragment : Fragment() {

    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!
    private lateinit var postAdapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommunityBinding.inflate(inflater, container, false)
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

        replaceFragment(FreeBoardFragment())
        updateButtonState(binding.freeBoardButton)

        binding.freeBoardButton.setOnClickListener {
            replaceFragment(FreeBoardFragment())
            updateButtonState(binding.freeBoardButton)
        }
        binding.questionBoardButton.setOnClickListener {
            replaceFragment(QuestionBoardFragment())
            updateButtonState(binding.questionBoardButton)
        }
        binding.tradeBoardButton.setOnClickListener {
            replaceFragment(TradeBoardFragment())
            updateButtonState(binding.tradeBoardButton)
        }

        binding.writePostButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.boardContainer, WritePostFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.searchButton.setOnClickListener {
            val query = binding.searchEditText.text.toString()
            if (query.isNotEmpty()) {
                performSearch(query)
            } else {
                Toast.makeText(context, "검색어를 입력하세요", Toast.LENGTH_SHORT).show()
            }
        }

        // RecyclerView 설정
        binding.searchRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.searchRecyclerView.adapter = postAdapter
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.boardContainer, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    private fun updateButtonState(activeButton: Button) {
        val buttons = listOf(binding.freeBoardButton, binding.questionBoardButton, binding.tradeBoardButton)
        buttons.forEach { button ->
            if (button == activeButton) {
                button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue))
                button.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            } else {
                button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
                button.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            }
        }
    }

    private fun performSearch(query: String) {
        Log.d("CommunityFragment", "Performing search for: $query")
        ApiObject.getRetrofitService.searchBoards(query, "자유게시판", 0).enqueue(object : Callback<PagedBoardResponse> {
            override fun onResponse(call: Call<PagedBoardResponse>, response: Response<PagedBoardResponse>) {
                Log.d("CommunityFragment", "Search response received")
                if (response.isSuccessful) {
                    response.body()?.let { pagedResponse ->
                        Log.d("CommunityFragment", "Search results: ${pagedResponse.content}")
                        if (pagedResponse.content.isNotEmpty()) {
                            binding.searchRecyclerView.visibility = View.VISIBLE
                            postAdapter.setPosts(pagedResponse.content)
                        } else {
                            binding.searchRecyclerView.visibility = View.GONE
                            Toast.makeText(context, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
                            postAdapter.setPosts(emptyList()) // 검색 결과가 없을 경우 빈 리스트 설정
                        }
                    }
                } else {
                    Log.e("CommunityFragment", "Search failed: ${response.errorBody()?.string()}")
                    Toast.makeText(context, "검색 결과를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
                    postAdapter.setPosts(emptyList()) // 검색 실패 시 빈 리스트 설정
                }
            }

            override fun onFailure(call: Call<PagedBoardResponse>, t: Throwable) {
                Log.e("CommunityFragment", "Search network error: ${t.message}")
                Toast.makeText(context, "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
                postAdapter.setPosts(emptyList()) // 네트워크 오류 시 빈 리스트 설정
            }
        })
    }

    fun updateUpvoteForPost(postId: Long) {
        postAdapter.updateUpvote(postId)
    }
    fun updateCommentCountForPost(postId: Long) {
        // 서버에서 게시글의 최신 데이터를 가져와 댓글 수를 업데이트하는 방식으로 처리
        ApiObject.getRetrofitService.getBoardsByPage("자유게시판", 0, 10).enqueue(object : Callback<PagedBoardResponse> {
            override fun onResponse(call: Call<PagedBoardResponse>, response: Response<PagedBoardResponse>) {
                if (response.isSuccessful) {
                    val updatedPost = response.body()?.content?.find { it.id.toLong() == postId }
                    updatedPost?.let {
                        postAdapter.updateCommentCount(postId, it.commentCount)
                    }
                } else {
                    Toast.makeText(context, "게시글 데이터를 가져오는데 실패했습니다. 오류 코드: ${response.code()}", Toast.LENGTH_SHORT).show()
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
