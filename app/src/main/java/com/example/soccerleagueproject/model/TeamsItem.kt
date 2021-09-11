package com.example.soccerleagueproject.model


import com.google.gson.annotations.SerializedName

data class TeamsItem(
    @SerializedName("id")
    val id: String,
    @SerializedName("teamName")
    val teamName: String
)