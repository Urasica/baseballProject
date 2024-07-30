package com.example.baseballapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FreeBoardFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private val boardDataList = mutableListOf<BoardData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_free_board, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        postAdapter = PostAdapter(boardDataList)
        recyclerView.adapter = postAdapter

        loadBoardData()
    }

    private fun loadBoardData() {
        ApiObject.getRetrofitService.getAllBoards().enqueue(object : Callback<List<BoardData>> {
            override fun onResponse(call: Call<List<BoardData>>, response: Response<List<BoardData>>) {
                if (response.isSuccessful) {
                    val boardList = response.body()
                    if (boardList != null) {
                        postAdapter.setPosts(boardList)
                    }
                } else {
                    Toast.makeText(context, "Failed to load posts", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<BoardData>>, t: Throwable) {
                Toast.makeText(context, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
