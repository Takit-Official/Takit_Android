package com.android.takit.data.local

import android.content.Context
import android.content.SharedPreferences

object TakitAuthSharedPreferences {
    private const val ACCESS_TOKEN = "ACCESS_TOKEN"


    private lateinit var authPreferences: SharedPreferences

    fun init(context: Context) {
        authPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    }

    fun setAccessToken(accessToken: String) {
        authPreferences.edit().putString(ACCESS_TOKEN, accessToken).apply()
    }

    fun getAccessToken() = authPreferences.getString(ACCESS_TOKEN, "") ?: ""
}