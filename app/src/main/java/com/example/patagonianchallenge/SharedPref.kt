package com.example.patagonianchallenge

import android.content.SharedPreferences

import android.app.Activity
import android.content.Context


object SharedPref {
    private var mSharedPref: SharedPreferences? = null
    const val sessionCount = "session_count"

    fun init(context: Context) {
        if (mSharedPref == null) mSharedPref =
            context.getSharedPreferences(context.packageName, Activity.MODE_PRIVATE)
    }

    fun read(key: String, defValue: Int): Int {
        return mSharedPref!!.getInt(key, defValue)
    }

    fun write(key: String?, value: Int?) {
        val prefsEditor = mSharedPref!!.edit()
        prefsEditor.putInt(key, value!!).apply()
    }
}