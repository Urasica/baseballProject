package com.example.baseballapp.community

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.baseballapp.CommentData
import com.example.baseballapp.R

class CommentAdapter(private var commentList: List<CommentData>) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImage: ImageView = itemView.findViewById(R.id.iv_user_image)
        val author: TextView = itemView.findViewById(R.id.tv_comment_author)
        val content: TextView = itemView.findViewById(R.id.tv_comment_content)
        val createdAt: TextView = itemView.findViewById(R.id.tv_comment_created_at)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = commentList[position]
        holder.author.text = comment.authorId
        holder.content.text = comment.content
        holder.createdAt.text = comment.createdAt.substring(0, 10)
    }

    override fun getItemCount(): Int = commentList.size

    fun setComments(comments: List<CommentData>) {
        commentList = comments
        notifyDataSetChanged()
    }
}
