package com.example.myapplication

sealed class Screens(val screen:String){
    data object SplashScreen : Screens(screen = "splashscreen")
    data object Login : Screens(screen = "login")
    data object Home:Screens(screen = "home")
    data object Notification:Screens(screen = "notification")
    data object Requests:Screens(screen = "requests")
    data object Chatbot:Screens(screen = "chatbot")
    data object TeamsSchedule:Screens(screen = "teamsschedule")
}