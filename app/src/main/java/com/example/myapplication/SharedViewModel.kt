
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.yourapp.ui.Request

class SharedViewModel : ViewModel() {
    val logoSize = mutableStateOf(237.dp)
    val userId: MutableLiveData<String> = MutableLiveData()
    val userToken: MutableLiveData<String> = MutableLiveData()
    val userName: MutableLiveData<String> = MutableLiveData()
    val userRequests: MutableLiveData<List<Request>> = MutableLiveData()

    fun setUserInfo(id: String) {
        userId.value = id
//        userToken.value = token
//        userName.value = name
    }

    fun setUserRequests(requests: List<Request>) {
        userRequests.value = requests
    }
}
