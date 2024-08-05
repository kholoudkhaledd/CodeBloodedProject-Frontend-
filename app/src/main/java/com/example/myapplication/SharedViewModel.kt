
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val logoSize = mutableStateOf(237.dp)
}
