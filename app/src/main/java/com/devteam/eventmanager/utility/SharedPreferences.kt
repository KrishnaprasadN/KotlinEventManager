package com.devteam.eventmanager.utility

import android.content.Context
import android.content.SharedPreferences
import com.devteam.eventmanager.R

class SharedPreferences {
    fun getSharedPreferences(context : Context?) : SharedPreferences? {
        return context?.getSharedPreferences(
            context.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
    }

    fun isLoogedIn(context : Context?) : Boolean?{
        return getSharedPreferences(context)?.getBoolean(context?.getString(R.string.preference_is_logged_in_key),false)
    }

    fun loogedIn(context : Context?) {
         val editableSharedPreferences = getSharedPreferences(context)?.edit()
         editableSharedPreferences?.putBoolean(context?.getString(R.string.preference_is_logged_in_key),true)
         editableSharedPreferences?.commit()
    }

    fun loogedOut(context : Context?) {
        val editableSharedPreferences = getSharedPreferences(context)?.edit()
        editableSharedPreferences?.putBoolean(context?.getString(R.string.preference_is_logged_in_key),false)
        editableSharedPreferences?.commit()
    }
}