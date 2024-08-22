package com.example.myapplication.calander

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Retrofit.RetrofitClient
import kotlinx.coroutines.launch

class ScheduleViewModel : ViewModel() {
    private val _location = MutableLiveData<String>()
    val location: LiveData<String> = _location

    fun fetchDaySchedule(month: String, day: String, year: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getDaySchedule(month, day, year)
                if (response.isSuccessful) {
                    val data = response.body()
                    _location.value = data?.location ?: "Unknown"
                } else {
                    _location.value = "Error fetching data"
                }
            } catch (e: Exception) {
                _location.value = "Error: ${e.message}"
            }
        }
    }
}
