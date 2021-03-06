package com.example.soccerleagueproject.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.soccerleagueproject.R
import com.example.soccerleagueproject.model.Fixture

class FixtureAdapter: RecyclerView.Adapter<FixtureAdapter.FixturesViewHolder>() {

    var fixtureList:List<Fixture>?=null
    class FixturesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewHomeName = itemView.findViewById<TextView>(R.id.home_team_name)
        val textViewAwayName=itemView.findViewById<TextView>(R.id.away_team_name)
        fun bind(fixture: Fixture) {
            textViewHomeName.text = fixture.homeTeam.teamName
            textViewAwayName.text=fixture.awayTeam.teamName
        }
    }
    fun setFixtureList(fixtureList: ArrayList<Fixture>) {
        this.fixtureList = fixtureList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FixturesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fixture, parent, false)
        return FixturesViewHolder(view)
    }

    override fun onBindViewHolder(holder: FixturesViewHolder, position: Int) {
        fixtureList?.get(position)?.let { holder.bind(it) }
    }

    override fun getItemCount()=fixtureList?.size?:0


}