package com.example.soccerleagueproject.UI.List

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.soccerleagueproject.Adapters.ListViewAdapter
import com.example.soccerleagueproject.R

class TeamListFragment : Fragment() {

    private val viewModel:TeamListViewModel by viewModels()
    private var adapter=ListViewAdapter()
    private lateinit var recycler:RecyclerView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getTeams()

        recycler=view.findViewById(R.id.recycler)
        recycler.layoutManager=LinearLayoutManager(context)
        recycler.adapter=adapter

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.showListLiveData.observe(viewLifecycleOwner) {
            adapter.apply {
                teamList = it
                notifyDataSetChanged()
            }
        }
        viewModel.errorStateLiveData.observe(this) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        } }
}