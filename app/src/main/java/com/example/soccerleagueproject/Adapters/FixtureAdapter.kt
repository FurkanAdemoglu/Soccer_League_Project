package com.example.soccerleagueproject.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.soccerleagueproject.R
import com.example.soccerleagueproject.model.Fixture
import com.example.soccerleagueproject.model.TeamsItem

class FixtureAdapter: RecyclerView.Adapter<FixtureAdapter.ListViewHolder>() {

    var fixtureList:List<Fixture>?=null
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val containerCardView = itemView.findViewById<CardView>(R.id.fixtureCardView)
        val textViewHomeName = itemView.findViewById<TextView>(R.id.home_team_name)
        val textViewAwayName=itemView.findViewById<TextView>(R.id.away_team_name)
        fun bind(fixture: Fixture) {
            textViewHomeName.text = fixture.homeTeam.toString()
            textViewAwayName.text=fixture.awayTeam.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fixture, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        fixtureList?.get(position)?.let { holder.bind(it) }
    }

    override fun getItemCount()=fixtureList?.size?:0


}