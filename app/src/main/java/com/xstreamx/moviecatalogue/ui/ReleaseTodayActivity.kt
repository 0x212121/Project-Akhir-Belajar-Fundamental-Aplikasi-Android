package com.xstreamx.moviecatalogue.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xstreamx.moviecatalogue.R
import com.xstreamx.moviecatalogue.adapter.FavoriteMovieAdapter
import com.xstreamx.moviecatalogue.model.Movie
import kotlinx.android.synthetic.main.activity_release_today.*

class ReleaseTodayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_release_today)

        supportActionBar?.title = getString(R.string.today_release)

        progressBar_release_today.visibility = View.VISIBLE

        if (intent.getSerializableExtra("movieList") != null) {
            showMovies()
        }
    }

    fun showMovies() {
        val movies = intent.getSerializableExtra("movieList") as ArrayList<Movie>
        rv_release_today.apply {
            adapter = FavoriteMovieAdapter(movies, applicationContext, object : FavoriteMovieAdapter.OnItemClicked {
                override fun onItemClick(position: Int) {
                    val intent = Intent(applicationContext, DetailActivity::class.java)
                    intent.putExtra("extra_id", movies[position].id)
                    intent.putExtra("extra_type", "movie")
                    startActivity(intent)
                }
            })

            layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        }
        progressBar_release_today.visibility = View.GONE

    }


}
