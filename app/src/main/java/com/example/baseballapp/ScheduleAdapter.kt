package com.example.baseballapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ScheduleAdapter(private val scheduleList: List<ScheduleData>) : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    inner class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val team1TextView: TextView = itemView.findViewById(R.id.team1TextView)
        val team2TextView: TextView = itemView.findViewById(R.id.team2TextView)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_schedule, parent, false)
        return ScheduleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val currentItem = scheduleList[position]
        holder.team1TextView.text = currentItem.team1
        holder.team2TextView.text = currentItem.team2
        holder.dateTextView.text = currentItem.date
    }

    override fun getItemCount() = scheduleList.size
}

