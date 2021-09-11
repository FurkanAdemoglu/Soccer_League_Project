package com.example.soccerleagueproject.util

import com.example.soccerleagueproject.Network.TeamShowService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceManager {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://613c3bc0270b96001798b060.mockapi.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(TeamShowService::class.java)
}