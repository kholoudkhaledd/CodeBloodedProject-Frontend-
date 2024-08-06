import java.lang.reflect.Modifier
import java.nio.file.WatchEvent.Modifier

@Composable
fun ManagerRequest(requests: List<Request>)
{
    var requestList by remember { mutableStateOf(requests) }
   Column(
       modifier=Modifier.padding(16.dp)

   ){
        Card(
            shape = RoundedCornerShape(40.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent),
            colors = CardDefaults.cardColors(containerColor = Color.White),
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "My Requests",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .padding(top = 10.dp)
                )
}