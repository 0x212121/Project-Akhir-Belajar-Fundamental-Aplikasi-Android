package com.xstreamx.moviecatalogue.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.xstreamx.moviecatalogue.notification.AlarmReceiver

import com.xstreamx.moviecatalogue.R

class SettingFragment : PreferenceFragmentCompat() {

    private var alarmReceiver = AlarmReceiver()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        val sharedPref = SettingPreference(context as Context).getInstance(context as Context)

        val dailyReminderSwitch = findPreference<SwitchPreferenceCompat>("daily_reminder")
        val releaseReminderSwitch = findPreference<SwitchPreferenceCompat>("release_reminder")
        val languagePreference = findPreference<Preference>("preference_language")


        dailyReminderSwitch?.isChecked = sharedPref.checkDailyReminder() == true

        releaseReminderSwitch?.isChecked = sharedPref.checkReleaseReminder() == true

        dailyReminderSwitch?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { preference, _ ->
                val dailySwitch = preference as SwitchPreferenceCompat

                if (dailySwitch.isChecked){
                    sharedPref.setDailyReminder(false)
                    alarmReceiver.cancelAlarm(context as Context, AlarmReceiver().TYPE_DAILY)

                } else{
                    sharedPref.setDailyReminder(true)
                    alarmReceiver.setRepeatingAlarm(context as Context, AlarmReceiver().TYPE_DAILY, "07:00",
                        getString(R.string.daily_notif_message))

                }
                true
            }

        releaseReminderSwitch?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { preference, _ ->
                val releaseSwitch = preference as SwitchPreferenceCompat

                if (releaseSwitch.isChecked){
                    sharedPref.setReleaseReminder(false)
                    alarmReceiver.cancelAlarm(context as Context, AlarmReceiver().TYPE_RELEASE)


                } else{
                    sharedPref.setReleaseReminder(true)
                    alarmReceiver.setRepeatingAlarm(context as Context, AlarmReceiver().TYPE_RELEASE, "08:00",
                        getString(R.string.release_notif_message))
                }
                true
            }

        languagePreference?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
            true
        }
    }
}