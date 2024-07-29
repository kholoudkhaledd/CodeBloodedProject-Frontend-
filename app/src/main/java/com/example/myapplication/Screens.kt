package com.example.myapplication

sealed class Screens(val screen:String){
    data object Home:Screens(screen = "home")
    data object Notification:Screens(screen = "notification")
    data object Requests:Screens(screen = "requests")
    data object Chatbot:Screens(screen = "chatbot")
}