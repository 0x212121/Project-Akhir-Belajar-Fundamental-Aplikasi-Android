package com.xstreamx.moviecatalogue.ui.favorite


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xstreamx.moviecatalogue.ui.DetailActivity

import com.xstreamx.moviecatalogue.R
import com.xstreamx.moviecatalogue.adapter.FavoriteMovieAdapter
import com.xstreamx.moviecatalogue.db.MovieHelper
import com.xstreamx.moviecatalogue.model.Movie
import kotlinx.android.synthetic.main.fragment_favorite_movie.*

class FavoriteMovieFragment : Fragment() {

    lateinit var movieHelper: MovieHelper
    lateinit var favoriteMovies: ArrayList<Movie>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieHelper = MovieHelper.getInstance(context!!)
        movieHelper.open()

        favoriteMovies = movieHelper.getAllMovie()
        Log.d("track fav", favoriteMovies.toString())

        val reverseLinearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,
            true)
        rv_favorite_movie.apply {
            adapter = FavoriteMovieAdapter(favoriteMovies, context, object : FavoriteMovieAdapter.OnItemClicked {
                override fun onItemClick(position: Int) {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra("extra_type", "movie_favorite")
                    intent.putExtra("extra_id", favoriteMovies[position].id)
                    intent.putExtra("fav_movie", favoriteMovies[position])
                    startActivity(intent)
                }
            })
            reverseLinearLayoutManager.stackFromEnd = true
            layoutManager = reverseLinearLayoutManager
        }
        progressBar_favorite_movie.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        movieHelper.open()
        favoriteMovies.clear()
        favoriteMovies.addAll(movieHelper.getAllMovie())
        rv_favorite_movie.adapter?.notifyDataSetChanged()
    }
}
