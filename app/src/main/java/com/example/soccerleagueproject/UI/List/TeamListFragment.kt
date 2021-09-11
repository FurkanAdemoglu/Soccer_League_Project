package com.example.soccerleagueproject.UI.List

import android.animation.Animator
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.soccerleagueproject.Adapters.ListViewAdapter
import com.example.soccerleagueproject.R
import com.example.soccerleagueproject.databinding.FragmentTeamListBinding

class TeamListFragment : Fragment(R.layout.fragment_team_list) {

    private val viewModel:TeamListViewModel by viewModels()
    private lateinit var _binding:FragmentTeamListBinding
    private var adapter=ListViewAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamListBinding.inflate(inflater, container, false)
        return _binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getTeams()
        initView()
        observeViewModel()
    }
    private fun initView(){
        _binding.teamRecycler.layoutManager=LinearLayoutManager(context)
        _binding.teamRecycler.adapter=adapter
        _binding.drawFixtureButton.setOnClickListener {
            _binding.listToFixtureAnimation.visibility = View.VISIBLE
            _binding.teamRecycler.visibility=View.GONE
            _binding.drawFixtureButton.visibility=View.GONE
            _binding.listToFixtureAnimation.setAnimation(R.raw.soccer)
            _binding.listToFixtureAnimation.playAnimation()
            _binding.listToFixtureAnimation.addAnimatorListener(object :
                Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                    Log.v("AnimationStarted", "Started")
                }
                override fun onAnimationEnd(animation: Animator?) {
                    val action=TeamListFragmentDirections.actionTeamListFragmentToFixturesFragment()
                    findNavController().navigate(action)
                }
                override fun onAnimationCancel(animation: Animator?) {
                    Log.v("AnimationCanceled", "Canceled")
                }
                override fun onAnimationRepeat(animation: Animator?) {
                    Log.v("AnimationRepeated", "Repeated") } }
            )

        }
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