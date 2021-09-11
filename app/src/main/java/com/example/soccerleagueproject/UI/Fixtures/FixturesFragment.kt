package com.example.soccerleagueproject.UI.Fixtures

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.soccerleagueproject.Adapters.ListViewAdapter
import com.example.soccerleagueproject.R
import com.example.soccerleagueproject.UI.List.TeamListViewModel
import com.example.soccerleagueproject.databinding.FragmentFixturesBinding


class FixturesFragment : Fragment(R.layout.fragment_fixtures) {
    private val viewModel: FixtureCreateViewModel by viewModels()
    private lateinit var _binding: FragmentFixturesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFixturesBinding.inflate(inflater, container, false)
        return _binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Toast.makeText(context, "Fikstür sayfası", Toast.LENGTH_SHORT).show()

    }
}