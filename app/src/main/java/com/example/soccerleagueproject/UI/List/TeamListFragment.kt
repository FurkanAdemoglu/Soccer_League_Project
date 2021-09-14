package com.example.soccerleagueproject.UI.List

import android.animation.Animator
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.soccerleagueproject.Adapters.ListViewAdapter
import com.example.soccerleagueproject.R
import com.example.soccerleagueproject.databinding.FragmentTeamListBinding
import java.io.Serializable

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
        checkTheme()
        _binding.changeTheme.setOnClickListener { chooseThemeDialog() }
    }

    private fun checkTheme() {
        when (MyPreferences(requireContext()).darkMode) {
            0 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

            }
            1 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

            }
            2 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            } } }
    private fun chooseThemeDialog() {

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.choose_theme_text))
        val styles = arrayOf("Light","Dark","System default")
        val checkedItem = MyPreferences(requireContext()).darkMode

        builder.setSingleChoiceItems(styles, checkedItem) { dialog, which ->
            when (which) {
                0 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    MyPreferences(requireContext()).darkMode = 0
                    dialog.dismiss()
                }
                1 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    MyPreferences(requireContext()).darkMode = 1
                    dialog.dismiss()
                }
                2 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    MyPreferences(requireContext()).darkMode = 2
                    dialog.dismiss()
                }

            }
        }
        val dialog = builder.create()
        dialog.show()
    }
    private fun initView(){
        _binding.teamRecycler.layoutManager=LinearLayoutManager(context)
        _binding.teamRecycler.adapter=adapter
        _binding.drawFixtureButton.setOnClickListener {
            _binding.listToFixtureAnimation.visibility = View.VISIBLE
            _binding.teamRecycler.visibility=View.GONE
            _binding.changeTheme.visibility=View.GONE
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

class MyPreferences(context: Context?) {

    companion object {
        private const val DARK_STATUS = "io.github.manuelernesto.DARK_STATUS"
    }

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    var darkMode = preferences.getInt(DARK_STATUS, 0)
        set(value) = preferences.edit().putInt(DARK_STATUS, value).apply()

}


