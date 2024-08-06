
import androidx.compose.runtime.Composable
import com.google.firebase.Firebase
import com.google.firebase.database.database

@Composable
fun FirebaseConnection() {

    val database = Firebase.database
    val myRef = database.getReference("message")
}
