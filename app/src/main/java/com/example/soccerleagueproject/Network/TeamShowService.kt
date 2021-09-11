package com.example.soccerleagueproject.Network

import com.example.soccerleagueproject.model.Teams
import retrofit2.http.GET
import retrofit2.http.Query

interface TeamShowService {

    @GET("teamNames")
    suspend fun getTeamNames():Teams
}