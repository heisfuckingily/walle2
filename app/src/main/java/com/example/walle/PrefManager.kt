package com.example.walle

import android.content.Context
import android.content.SharedPreferences

class PrefManager(context: Context) {
    private var sharedPreferences: SharedPreferences
    private var editor: SharedPreferences.Editor

    private val _prefName: String = "IntroScreen"
    val _isFirstLaunch: String = "IsFirstLaunchX"

    init {
        sharedPreferences = context.getSharedPreferences(_prefName, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    fun SetIsFirstLaunch(flag: Boolean) {
        editor.putBoolean(_isFirstLaunch, flag)
        editor.commit()
    }

    fun IsFirstLaunch(): Boolean {
        return sharedPreferences.getBoolean(_isFirstLaunch, true)
    }
}