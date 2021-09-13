package com.example.soccerleagueproject.Network

import com.example.soccerleagueproject.model.Teams
import com.example.soccerleagueproject.model.TeamsItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TeamShowService {

    @GET("teamNames")
    suspend fun getTeamNames():Teams

    @GET("teamNames")
    fun getTeams(): Call<List<TeamsItem>>
}