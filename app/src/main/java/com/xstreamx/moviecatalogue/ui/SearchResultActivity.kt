package com.xstreamx.moviecatalogue.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xstreamx.moviecatalogue.R
import com.xstreamx.moviecatalogue.ui.movie.MovieViewModel
import com.xstreamx.moviecatalogue.ui.tvshow.TvShowViewModel
import com.xstreamx.moviecatalogue.adapter.MovieAdapter
import com.xstreamx.moviecatalogueapi.adapter.TVShowAdapter
import com.xstreamx.moviecatalogue.model.Movie
import kotlinx.android.synthetic.main.activity_search_result.*

class SearchResultActivity : AppCompatActivity() {

//    companion object {
        private val listMovies = MutableLiveData<ArrayList<Movie>>()
        private lateinit var movieViewModel: MovieViewModel
        private lateinit var tvShowViewModel: TvShowViewModel
        private lateinit var movieAdapter: MovieAdapter
        private lateinit var tvShowAdapter: TVShowAdapter
        private lateinit var query: String
    private lateinit var type: String
        val EXTRA_QUERY = "extra_query"
    val EXTRA_TYPE = "extra_type"
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)
        showLoading(true)

        query = intent.getStringExtra(EXTRA_QUERY)
        type = intent.getStringExtra(EXTRA_TYPE)

        when (type){
            "movie" -> {
                movieViewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)
                searchMovie()
                supportActionBar?.title = "Movie Search Results"
            }
            "tv" -> {
                tvShowViewModel = ViewModelProviders.of(this).get(TvShowViewModel::class.java)
                searchTVShow()
                supportActionBar?.title = "TV Show Search Results"
            }
        }
    }

    fun searchMovie() {
        movieViewModel.getMovies().observe(this@SearchResultActivity, Observer { movie ->
            if (movie != null) {
                movieAdapter = MovieAdapter()
                movieAdapter.setData(movie)
                Log.d("track movie: ", movie.toString())
                rv_search_result.apply {
                    adapter = movieAdapter
                    layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                }
            } else {
                tv_not_found_search_result.visibility = View.VISIBLE
            }
            showLoading(false)
        })
        movieViewModel.searchMoviesTVShows("movie", query)
    }

    fun searchTVShow() {
        tvShowViewModel.getTVShows().observe(this@SearchResultActivity, Observer { tvShow ->
            if (tvShow != null) {
                tvShowAdapter = TVShowAdapter()
                tvShowAdapter.setData(tvShow)
                Log.d("track movie: ", tvShow.toString())
                rv_search_result.apply {
                    adapter = tvShowAdapter
                    layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                }
            } else {
                tv_not_found_search_result.visibility = View.VISIBLE
            }
            showLoading(false)
        })
        tvShowViewModel.searchTVShows("tv", query)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("query", query)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        query = savedInstanceState?.getString("query").toString()

    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar_search_result.visibility = View.VISIBLE
        } else {
            progressBar_search_result.visibility = View.GONE
        }
    }

}