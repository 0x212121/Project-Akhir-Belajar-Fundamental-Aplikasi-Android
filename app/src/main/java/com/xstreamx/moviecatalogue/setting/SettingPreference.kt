package com.xstreamx.moviecatalogue.setting

import android.content.Context
import android.content.SharedPreferences

class SettingPreference(val context: Context) {

    private val SHARED_PREF_NAME = "moviecataloguefinale"
    private val KEY_INIT = "init"
    private val KEY_DAILY = "daily_reminder"
    private val KEY_RELEASE = "release_reminder"

    private var mInstance: SettingPreference? = null
    private val sharedPreferences: SharedPreferences? = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

    @Synchronized
    fun getInstance(context: Context): SettingPreference {
        if (mInstance == null) {
            mInstance = SettingPreference(context)
        }
        return mInstance as SettingPreference
    }

    fun setDailyReminder(daily: Boolean){
        val editor = sharedPreferences?.edit()

        editor?.putBoolean(KEY_DAILY, daily)
        editor?.putInt(KEY_INIT, 1)
        editor?.apply()
    }
    fun setReleaseReminder(release: Boolean){
        val editor = sharedPreferences?.edit()

        editor?.putBoolean(KEY_RELEASE, release)
        editor?.putInt(KEY_INIT, 1)
        editor?.apply()
    }


    fun checkDailyReminder() : Boolean?{
        return sharedPreferences?.getBoolean(KEY_DAILY, true)
    }

    fun checkReleaseReminder() : Boolean?{
        return sharedPreferences?.getBoolean(KEY_RELEASE, true)
    }

    fun checkInit(): Int? {
        return sharedPreferences?.getInt(KEY_INIT,0)
    }
}