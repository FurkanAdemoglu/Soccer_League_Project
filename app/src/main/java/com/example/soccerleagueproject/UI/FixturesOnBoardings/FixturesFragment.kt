package com.example.soccerleagueproject.UI.FixturesOnBoardings

import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionManager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.soccerleagueproject.Adapters.FixtureAdapter
import com.example.soccerleagueproject.Network.TeamShowService
import com.example.soccerleagueproject.R
import com.example.soccerleagueproject.UI.FixturesOnBoardings.utils.OnSwipeTouchListener
import com.example.soccerleagueproject.databinding.FragmentFixturesBinding
import com.example.soccerleagueproject.model.Fixture
import com.example.soccerleagueproject.model.TeamsItem
import com.example.soccerleagueproject.util.ServiceManager
import java.util.*
import kotlin.collections.ArrayList

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class FixturesFragment: Fragment(R.layout.fragment_fixtures) {
    private val viewModel: FixtureCreateViewModel by viewModels()
    private lateinit var binding: FragmentFixturesBinding
    private var adapter= FixtureAdapter()
    var fixture = arrayListOf<Fixture>()
    private var TOTAL_NUM_ITEMS = 0
    private var ITEMS_PER_PAGE = 0
    private var ITEMS_REMAINING = 0
    private var LAST_PAGE = 0
    private var currentPage = 0
    private var totalPages = 0


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

    }

    private fun observeViewModel(){
        viewModel.showListLiveData.observe(viewLifecycleOwner) {
        val rounds=getFixtures(it,true)

            for (i in rounds.indices) {
                for (fixtures in rounds[i]) {
                    fixture.add(fixtures)
                }
            }

            TOTAL_NUM_ITEMS = fixture.size
            ITEMS_PER_PAGE = fixture.size / rounds.size
            ITEMS_REMAINING = TOTAL_NUM_ITEMS % ITEMS_PER_PAGE
            LAST_PAGE = TOTAL_NUM_ITEMS / ITEMS_PER_PAGE
            Log.v("Fixtures","$ITEMS_PER_PAGE")
            Log.v("FixturesSize","$TOTAL_NUM_ITEMS")
            totalPages = TOTAL_NUM_ITEMS / ITEMS_PER_PAGE
            adapter.setFixtureList(generatePage(currentPage) as ArrayList<Fixture>)
            binding.week.setText("Week " + (currentPage + 1).toString())
            binding.rvFixture.setOnTouchListener(object : OnSwipeTouchListener(requireContext()) {
                override fun onSwipeRight() {
                    if (currentPage == totalPages) {
                        currentPage -= 1
                       binding.week.setText("Week " + (currentPage + 1).toString())
                        val slide = Slide()
                        slide.slideEdge = Gravity.END
                        TransitionManager.beginDelayedTransition(binding.rvFixture, slide)
                       adapter.setFixtureList(generatePage(currentPage) as ArrayList<Fixture>)
                    } else if (currentPage >= 1 && currentPage < totalPages) {
                        currentPage -= 1
                        binding.week.setText("Week " + (currentPage + 1).toString())
                        val slide = androidx.transition.Slide()
                        slide.slideEdge = Gravity.END
                        androidx.transition.TransitionManager.beginDelayedTransition(binding.rvFixture, slide)
                        adapter.setFixtureList(generatePage(currentPage) as ArrayList<Fixture>)
                    } else {
                    }
                }

                override fun onSwipeLeft() {
                    if (currentPage == 0) {
                        currentPage += 1
                        binding.week.setText("Week " + (currentPage + 1).toString())
                        val slide = androidx.transition.Slide()
                        slide.slideEdge = Gravity.START
                        androidx.transition.TransitionManager.beginDelayedTransition(binding.rvFixture, slide)
                        adapter.setFixtureList(generatePage(currentPage) as ArrayList<Fixture>)
                    } else if (currentPage >= 1 && currentPage < totalPages) {
                        currentPage += 1
                        if (currentPage == totalPages) {
                            binding.week.setText("Week $currentPage")
                        } else {
                            binding.week.setText("Week " + (currentPage + 1).toString())
                        }
                        val slide = androidx.transition.Slide()
                        slide.slideEdge = Gravity.START
                        androidx.transition.TransitionManager.beginDelayedTransition(binding.rvFixture, slide)
                       adapter.setFixtureList(generatePage(currentPage) as ArrayList<Fixture>)
                    } else {
                    }
                }
            })
        }
        viewModel.errorStateLiveData.observe(this) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun generatePage(currentPage: Int): List<Fixture>? {
        val startItem = currentPage * ITEMS_PER_PAGE
        val numOfData = ITEMS_PER_PAGE
        val pageData = java.util.ArrayList<Fixture>()
        if (currentPage == LAST_PAGE && ITEMS_REMAINING > 0) {
            for (i in startItem until startItem + ITEMS_REMAINING) {
                pageData.add(fixture[i])
            }
        } else {
            for (i in startItem until startItem + numOfData) {
                pageData.add(fixture[i])
            }
        }
        return pageData
    }


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
        Log.v("TeamsSize","Response="+teams)
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
        for (roundNumber in rounds.indices+1) {
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


