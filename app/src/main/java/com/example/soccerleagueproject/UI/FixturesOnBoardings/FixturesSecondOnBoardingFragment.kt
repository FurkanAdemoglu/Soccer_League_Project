package com.example.soccerleagueproject.UI.FixturesOnBoardings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.soccerleagueproject.R


class FixturesSecondOnBoardingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fixtures_second_on_boarding, container, false)
    }
}