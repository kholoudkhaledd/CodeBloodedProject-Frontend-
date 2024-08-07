package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourapp.ui.Request
import com.example.yourapp.ui.RequestStatus
import com.example.yourapp.ui.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RequestsViewModel : ViewModel() {
    private val _requests = MutableStateFlow<List<Request>>(emptyList())
    val requests: StateFlow<List<Request>> = _requests

    fun fetchRequests() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getRequests()
                _requests.value = response.map { requestDto ->
                    Request(
                        id = requestDto.requestID,
                        time = "${requestDto.changeDayFrom} to ${requestDto.changeDayTo}",
                        description = "You have submitted a request to change your scheduled office day from ${requestDto.changeDayFrom} to ${requestDto.changeDayTo}.",
                        status = RequestStatus.valueOf(requestDto.Status.uppercase())
                    )
                }
                println("Requests fetched: ${_requests.value}") // Debug log
            } catch (e: Exception) {
                println("Error fetching requests: ${e.message}") // Debug log
            }
        }
    }

    fun cancelRequest(request: Request) {
        viewModelScope.launch {
            try {
                RetrofitInstance.api.deleteRequest(request.id)
                // Update UI after deletion
                fetchRequests()
            } catch (e: Exception) {
                // Handle error
                println("Error canceling request: ${e.message}") // Debug log
            }
        }
    }
}
