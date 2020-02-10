package com.xstreamx.moviecatalogue.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xstreamx.moviecatalogue.R

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        loadFragment(SettingFragment())
    }

    private fun loadFragment(fragment: androidx.fragment.app.Fragment): Boolean {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container_settings, fragment).commit()
        return true
    }
}
