package com.example.soccerleagueproject.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.soccerleagueproject.R
import com.example.soccerleagueproject.model.TeamsItem

class ListViewAdapter: RecyclerView.Adapter<ListViewAdapter.ListViewHolder>() {
    var teamList:List<TeamsItem>?=null


    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName = itemView.findViewById<TextView>(R.id.name)
        fun bind(team: TeamsItem) {
            textViewName.text = team.teamName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_team, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        teamList?.get(position)?.let { holder.bind(it) }
    }

    override fun getItemCount()=teamList?.size?:0

}