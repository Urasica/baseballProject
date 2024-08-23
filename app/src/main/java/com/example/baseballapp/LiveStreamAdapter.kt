package com.example.baseballapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LiveStreamAdapter(private val liveStreamMessages: List<String>) :
    RecyclerView.Adapter<LiveStreamAdapter.LiveStreamViewHolder>() {

    inner class LiveStreamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageTextView: TextView = itemView.findViewById(R.id.tv_live_stream_message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LiveStreamViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_live_stream, parent, false)
        return LiveStreamViewHolder(view)
    }

    override fun onBindViewHolder(holder: LiveStreamViewHolder, position: Int) {
        holder.messageTextView.text = liveStreamMessages[position]
    }

    override fun getItemCount(): Int = liveStreamMessages.size
}
