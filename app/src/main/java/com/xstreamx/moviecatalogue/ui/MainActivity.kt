package com.xstreamx.moviecatalogue.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.xstreamx.moviecatalogue.R
import com.xstreamx.moviecatalogue.notification.AlarmReceiver
import com.xstreamx.moviecatalogue.setting.SettingActivity
import com.xstreamx.moviecatalogue.setting.SettingPreference
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_favorites.*


class MainActivity : AppCompatActivity() {

    private val alarmReceiver = AlarmReceiver()

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_TYPE = "extra_type"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.navigation_movie,
            R.id.navigation_tvshow,
            R.id.navigation_favorite
        ).build()

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        bottom_navigation.setItemIconTintList(null)
        supportActionBar?.elevation = 0.0f

        val sharedPref = SettingPreference(applicationContext).getInstance(applicationContext)

        if (sharedPref.checkInit() == 0){
            sharedPref.setDailyReminder(true)
            sharedPref.setReleaseReminder(true)
            alarmReceiver.setRepeatingAlarm(applicationContext, AlarmReceiver().TYPE_DAILY, "07:00",
                getString(R.string.daily_notif_message))
            alarmReceiver.setRepeatingAlarm(applicationContext, AlarmReceiver().TYPE_RELEASE, "08:00",
                getString(R.string.release_notif_message))

        }

        if (intent.extras != null) {
            val type = intent.getStringExtra(EXTRA_TYPE)
            if (type == "release"){
                val intent = Intent(applicationContext, ReleaseTodayActivity::class.java)
                intent.putExtra("movieList", this.intent.getSerializableExtra("movieList"))
                startActivity(intent)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        if (searchManager != null) {
            val searchView =
                (menu.findItem(R.id.search)?.actionView) as SearchView
                searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
                searchView.queryHint = resources.getString(R.string.search_hint)
                searchView.setOnQueryTextListener(object :
                SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    //dapatkan item bottom navigation yang dipilih
                    if (bottom_navigation.selectedItemId == R.id.navigation_movie){
                        Log.d("track: ", "Nav Movie")
                        val intent = Intent(applicationContext, SearchResultActivity::class.java)
                        intent.putExtra(SearchResultActivity().EXTRA_QUERY, query)
                        intent.putExtra(SearchResultActivity().EXTRA_TYPE, "movie")
                        startActivity(intent)
                    } else if (bottom_navigation.selectedItemId == R.id.navigation_tvshow) {
                        Log.d("track: ", "Nav Tvshow")
                        val intent = Intent(applicationContext, SearchResultActivity::class.java)
                        intent.putExtra(SearchResultActivity().EXTRA_QUERY, query)
                        intent.putExtra(SearchResultActivity().EXTRA_TYPE, "tv")
                        startActivity(intent)
                    } else if (bottom_navigation.selectedItemId == R.id.navigation_favorite) {
                        val position = view_pager_favorite.currentItem
                        Log.d("track pos: ", position.toString())
                        view_pager_favorite.setCurrentItem(position, true)
                        if (position == 0) {
                            val intent = Intent(applicationContext, SearchResultActivity::class.java)
                            intent.putExtra(SearchResultActivity().EXTRA_QUERY, query)
                            intent.putExtra(SearchResultActivity().EXTRA_TYPE, "movie")
                            startActivity(intent)
                        } else {
                            val intent = Intent(applicationContext, SearchResultActivity::class.java)
                            intent.putExtra(SearchResultActivity().EXTRA_QUERY, query)
                            intent.putExtra(SearchResultActivity().EXTRA_TYPE, "tv")
                            startActivity(intent)
                        }
                    }
                    return true
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings){
            startActivity(Intent(applicationContext, SettingActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}
