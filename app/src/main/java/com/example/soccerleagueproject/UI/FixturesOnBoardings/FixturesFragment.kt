package com.example.soccerleagueproject.UI.FixturesOnBoardings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restaurantapplicationgraduationproject.ui.onBoarding.utils.OnBoardingAdapter
import com.example.restaurantapplicationgraduationproject.ui.onBoarding.utils.ZoomOutPageTransformer
import com.example.soccerleagueproject.Adapters.FixtureAdapter
import com.example.soccerleagueproject.Adapters.ListViewAdapter
import com.example.soccerleagueproject.Network.TeamShowService
import com.example.soccerleagueproject.R
import com.example.soccerleagueproject.UI.List.TeamListViewModel
import com.example.soccerleagueproject.databinding.FragmentFixturesBinding
import com.example.soccerleagueproject.model.Fixture
import com.example.soccerleagueproject.model.TeamsItem
import com.example.soccerleagueproject.util.ServiceManager
import java.util.*
import kotlin.collections.ArrayList

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import kotlin.math.round

class FixturesFragment: Fragment(R.layout.fragment_fixtures) {
    private val viewModel: FixtureCreateViewModel by viewModels()
    private lateinit var binding: FragmentFixturesBinding
    private var adapter= FixtureAdapter()
    var fixture = arrayListOf<Fixture>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFixturesBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getTeams()
        initViews()
        observeViewModel()




    }

    private fun initViews() {
        binding.rvFixture.layoutManager=LinearLayoutManager(context)
        binding.rvFixture.adapter=adapter
        val adapter = OnBoardingAdapter(requireActivity())
        binding.onboardingViewPager.adapter = adapter
        binding.onboardingViewPager.setPageTransformer(ZoomOutPageTransformer())
        binding.dotsIndicator.setViewPager2(binding.onboardingViewPager)
    }

    private fun observeViewModel(){
        viewModel.showListLiveData.observe(viewLifecycleOwner) {
        val rounds=getFixtures(it,true)
            for (i in rounds.indices) {
                for (fixtures in rounds[i]) {
                    fixture.add(fixtures)
                }
            }
            adapter.setFixtureList(fixture)

        }
        viewModel.errorStateLiveData.observe(this) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        } }


    var teamList: List<TeamsItem>? = null
    fun getFixtures(
        teams: List<TeamsItem?>,
        includeReverseFixtures: Boolean
    ): List<MutableList<Fixture>> {
        teamList = ArrayList<TeamsItem>()
        val apiService: TeamShowService = ServiceManager.service
        val call: Call<List<TeamsItem>> = apiService.getTeams()
        call.enqueue(object : Callback<List<TeamsItem>> {
            override fun onResponse(
                call: Call<List<TeamsItem>>,
                response: Response<List<TeamsItem>>
            ) {
                teamList=response.body()
                Log.v("Veriler","Response="+teamList)
            }

            override fun onFailure(call: Call<List<TeamsItem>>, t: Throwable) {
                Log.v("Hata Mesajı","Response="+t.toString())
            }

        })
        var numberOfTeams = teams.size
        var byeTeam = false
        if (numberOfTeams % 2 != 0) {
            numberOfTeams++
            byeTeam = true
        }
        val totalRounds = numberOfTeams - 1
        val matchesPerRound = numberOfTeams / 2 - 1
        var rounds: MutableList<MutableList<Fixture>> = LinkedList<MutableList<Fixture>>()
        for (round in 1 until totalRounds + 1) {
            val fixtures: MutableList<Fixture> = LinkedList<Fixture>()
            for (match in 1 until matchesPerRound + 1) {
                val home = (round + match) % (numberOfTeams - 1)
                var away = (numberOfTeams - 1 - match + round) % (numberOfTeams - 1)
                if (match == 0) {
                    away = numberOfTeams - 2
                }
                fixtures.add(Fixture(teams[home]!!, teams[away]!!))
            }
            rounds.add(fixtures)
        }
        val interleaved: MutableList<MutableList<Fixture>> = LinkedList<MutableList<Fixture>>()
        var evn = 0
        var odd = numberOfTeams / 2
        for (i in rounds.indices) {
            if (i % 2 == 0) {
                interleaved.add(rounds[evn++])
            } else {
                interleaved.add(rounds[odd++])
            }
        }
        rounds = interleaved
        for (roundNumber in rounds.indices) {
            if (roundNumber % 2 == 1) {
                val (homeTeam, awayTeam) = rounds[roundNumber][0]
                rounds[roundNumber][0] = Fixture(awayTeam, homeTeam)
            }
        }
        if (includeReverseFixtures) {
            val reverseFixtures: MutableList<MutableList<Fixture>> = LinkedList<MutableList<Fixture>>()
            for (round in rounds) {
                val reverseRound: MutableList<Fixture> = LinkedList<Fixture>()
                for ((homeTeam, awayTeam) in round) {
                    reverseRound.add(Fixture(awayTeam, homeTeam))
                }
                reverseFixtures.add(reverseRound)
            }
            rounds.addAll(reverseFixtures)
        }
        Log.v("Rounds", rounds.toString())
        return rounds
    }
}