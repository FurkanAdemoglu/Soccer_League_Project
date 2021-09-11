package com.example.soccerleagueproject.UI.List

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.soccerleagueproject.model.TeamsItem
import com.example.soccerleagueproject.util.ServiceManager
import com.example.soccerleagueproject.util.SingleLiveEvent
import kotlinx.coroutines.launch
import java.lang.Exception

class TeamListViewModel (application: Application) : AndroidViewModel(application){
    private val showListMutableLiveData = MutableLiveData<List<TeamsItem>>()
    val showListLiveData: LiveData<List<TeamsItem>> = showListMutableLiveData

    val errorStateLiveData = SingleLiveEvent<String>()

    fun getTeams(){
        viewModelScope.launch {
            try {
                val result = ServiceManager.service.getTeamNames()
                val showList = arrayListOf<TeamsItem>()
                for (showResult in result) {
                    showList.add(showResult)
                }
                showListMutableLiveData.postValue(showList)
            } catch (e: Exception) {
                errorStateLiveData.postValue("Bir hata oluştu")
                Log.v("hata", "service call error", e)
            }
        }
    }

}