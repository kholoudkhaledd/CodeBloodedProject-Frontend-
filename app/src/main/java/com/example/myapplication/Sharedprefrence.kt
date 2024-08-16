package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences

object Sharedpreference {
    private const val PREF_NAME = "my_app_prefs"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USER_TOKEN = "user_token"
    private const val KEY_User_Name = "user_name"

    private const val KEY_User_Position = "user_position"
    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
    fun saveUserName(context: Context, username: String) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(KEY_User_Name, username)
        editor.apply()
    }
    fun saveUserId(context: Context, userId: String) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(KEY_USER_ID, userId)
        editor.apply()
    }

    fun saveUserToken(context: Context, userToken: String) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(KEY_USER_TOKEN, userToken)
        editor.apply()
    }
    fun saveUserPosition(context: Context, position: String) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(KEY_User_Position, position)
        editor.apply()
    }


    fun getUserId(context: Context): String? {
        return getSharedPreferences(context).getString(KEY_USER_ID, null)
    }
    fun getUserName(context: Context): String? {
        return getSharedPreferences(context).getString(KEY_User_Name, null)
    }


    fun getUserToken(context: Context): String? {
        return getSharedPreferences(context).getString(KEY_USER_TOKEN, null)
    }
    fun getUserPosition(context: Context): String? {
        return getSharedPreferences(context).getString(KEY_User_Position, null)
    }
    fun removeUserToken(context: Context) {
        val editor = getSharedPreferences(context).edit()
        editor.remove(KEY_USER_TOKEN)
        editor.apply() // or editor.commit()
    }
    fun removeUserPosition(context: Context) {
        val editor = getSharedPreferences(context).edit()
        editor.remove(KEY_User_Position)
        editor.apply() // or editor.commit()
    }
    fun clearAll(context: Context) {
        val editor = getSharedPreferences(context).edit()
        editor.clear()
        editor.apply()
    }

}
