package com.example.baseballapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostAdapter(
    private var postList: List<BoardData>,
    private val onItemClick: (BoardData) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val author: TextView = itemView.findViewById(R.id.tv_author)
        val title: TextView = itemView.findViewById(R.id.tv_title)
        val createdAt: TextView = itemView.findViewById(R.id.tv_created_at)
        val comments: TextView = itemView.findViewById(R.id.tv_comments)
        val upVote: TextView = itemView.findViewById(R.id.tv_upvote)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view).apply {
            itemView.setOnClickListener {
                onItemClick(postList[adapterPosition])
            }
        }
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postList[position]
        holder.author.text = post.authorId
        holder.title.text = post.title
        holder.createdAt.text = post.createdAt.substring(0, 10)
        holder.comments.text = post.commentCount.toString()  // 댓글 수를 표시
        holder.upVote.text = post.upVote.toString()
    }

    override fun getItemCount(): Int = postList.size

    fun setPosts(posts: List<BoardData>) {
        postList = posts
        notifyDataSetChanged()
    }
    fun updateUpvote(postId: Long) {
        postList = postList.map { post ->
            if (post.id.toLong() == postId) {
                post.copy(upVote = post.upVote + 1)
            } else {
                post
            }
        }
        notifyDataSetChanged()
    }

    // 댓글 수를 업데이트하는 함수 (필요 시 사용)
    fun updateCommentCount(postId: Long, newCommentCount: Int) {
        postList = postList.map { post ->
            if (post.id.toLong() == postId) {
                post.copy(commentCount = newCommentCount)
            } else {
                post
            }
        }
        notifyDataSetChanged()
    }
}
