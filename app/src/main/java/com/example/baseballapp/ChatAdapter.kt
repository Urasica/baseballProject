package com.example.baseballapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(private val chatMessagesData: List<ChatMessageData>) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val teamLogo: ImageView = itemView.findViewById(R.id.teamLogo)
        val chatMessage: TextView = itemView.findViewById(R.id.chatMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chatMessage = chatMessagesData[position]
        holder.teamLogo.setImageResource(chatMessage.teamLogoResId)
        holder.chatMessage.text = chatMessage.message
    }

    override fun getItemCount(): Int {
        return chatMessagesData.size
    }
}
