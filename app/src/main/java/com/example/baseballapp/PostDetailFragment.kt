package com.example.baseballapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseballapp.databinding.FragmentPostDetailBinding
import com.example.login.LoginService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostDetailFragment : Fragment() {
    private val loginService by lazy { LoginService(requireContext()) }
    private var _binding: FragmentPostDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var post: BoardData
    private lateinit var commentAdapter: CommentAdapter

    companion object {
        private const val ARG_POST = "post"

        fun newInstance(post: BoardData) = PostDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_POST, post)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        post = arguments?.getParcelable(ARG_POST) ?: return

        binding.tvDetailTitle.text = post.title
        binding.tvDetailContent.text = post.content
        binding.tvDetailAuthor.text = post.authorId
        binding.tvDetailCreatedAt.text = post.createdAt.substring(0, 10)

        commentAdapter = CommentAdapter(emptyList())
        binding.rvComments.layoutManager = LinearLayoutManager(context)
        binding.rvComments.adapter = commentAdapter

        fetchComments(post.id.toLong())

        binding.btnSubmitComment.setOnClickListener {
            loginService.checkToken { isValid ->
                if (isValid) {
                    val commentContent = binding.etComment.text.toString()
                    if (commentContent.isNotEmpty()) {
                        submitComment(commentContent)
                    } else {
                        Toast.makeText(context, "댓글을 입력해주세요.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
        }

        binding.btnDetailDelete.setOnClickListener {
            loginService.checkToken { isValid ->
                if (isValid) {
                    deletePost(post.id.toLong())
                } else {
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
        }

        binding.btnDetailUpvote.setOnClickListener {
            upvotePost(post.id.toLong())
        }
    }

    private fun fetchComments(postId: Long) {
        ApiObject.getRetrofitService.getComments(postId).enqueue(object : Callback<List<CommentData>> {
            override fun onResponse(call: Call<List<CommentData>>, response: Response<List<CommentData>>) {
                if (response.isSuccessful) {
                    val comments = response.body() ?: emptyList()
                    commentAdapter.setComments(comments)
                } else {
                    Toast.makeText(context, "댓글을 불러오는데 실패했습니다. 오류 코드: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<CommentData>>, t: Throwable) {
                Toast.makeText(context, "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun upvotePost(postId: Long) {
        Log.d("PostDetailFragment", "Upvoting post with ID: $postId")
        ApiObject.getRetrofitService.upvotePost(postId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "추천하였습니다.", Toast.LENGTH_SHORT).show()
                    updateUpvoteInCommunity(postId)
                } else {
                    Toast.makeText(context, "추천에 실패했습니다. 오류 코드: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context, "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateUpvoteInCommunity(postId: Long) {
        val parentFragment = parentFragmentManager.findFragmentById(R.id.boardContainer)
        if (parentFragment is CommunityFragment) {
            parentFragment.updateUpvoteForPost(postId)
        }
    }

    private fun submitComment(content: String) {
        val newComment = CommentData(0, content, "사용자 이름", "2024-08-05T07:23:21.610Z", post.title)
        ApiObject.getRetrofitService.submitComment(post.id.toLong(), newComment).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    fetchComments(post.id.toLong()) // 댓글 목록 갱신
                    binding.etComment.text.clear()
                } else {
                    Toast.makeText(context, "댓글 작성에 실패했습니다. 오류 코드: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context, "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun updateCommentCountInCommunity(postId: Long) {
        val parentFragment = parentFragmentManager.findFragmentById(R.id.boardContainer)
        if (parentFragment is CommunityFragment) {
            parentFragment.updateCommentCountForPost(postId)
        }
    }
    private fun deletePost(postId: Long) {
        ApiObject.getRetrofitService.deletePost(postId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "게시글이 성공적으로 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                } else {
                    Toast.makeText(context, "게시글 삭제에 실패했습니다. 오류 코드: ${response.code()}", Toast.LENGTH_SHORT).show()
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
