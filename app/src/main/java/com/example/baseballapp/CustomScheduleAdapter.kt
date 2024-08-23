package com.example.baseballapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class CustomScheduleAdapter(private val context: Context, private val scheduleList: List<ScheduleData>) : BaseAdapter() {

    override fun getCount(): Int {
        return scheduleList.size
    }

    override fun getItem(position: Int): Any {
        return scheduleList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_schedule, parent, false)

        // 각각의 TextView를 레이아웃에서 가져옵니다.
        val team1TextView: TextView = view.findViewById(R.id.team1TextView)
        val team2TextView: TextView = view.findViewById(R.id.team2TextView)
        val dateTextView: TextView = view.findViewById(R.id.dateTextView)

        val schedule = scheduleList[position]

        // 데이터를 TextView에 설정합니다.
        team1TextView.text = schedule.team1
        team2TextView.text = schedule.team2
        dateTextView.text = schedule.date

        return view
    }
}
