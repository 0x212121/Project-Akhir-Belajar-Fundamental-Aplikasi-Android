package com.xstreamx.favoritemovie

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity(){

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_TYPE = "extra_type"
        var isFavorite = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        showLoading(true)

        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            DetailViewModel::class.java)

        movieHelper = MovieHelper.getInstance(applicationContext)
        movieHelper.open()
        tvShowHelper = TVShowHelper.getInstance(applicationContext)
        tvShowHelper.open()

        val id = intent.getStringExtra(EXTRA_ID)
        val type = intent.getStringExtra(EXTRA_TYPE)

        Log.d("track type: ", type)
        detailViewModel.setItems(id, type)

        when(type) {
            "movie" -> {
                supportActionBar?.setTitle(R.string.detail_movie)
                detailViewModel.getMovie().observe(this@DetailActivity, Observer { movie ->
                    if (movie != null) {
                        tv_title.text = movie.title
                        tv_overview.text = movie.description
                        tv_release_date.text = movie.release_date
                        rating.text = movie.score

                        Glide.with(applicationContext)
                            .load(BuildConfig.URL_IMAGES +movie.poster)
                            .apply(RequestOptions().override(350, 550))
                            .into(img_item_poster)

                        Glide.with(applicationContext)
                            .load(BuildConfig.URL_IMAGES +movie.backdrop)
                            .into(img_item_backdrop)

                        isFavorite = isFavorited(movie.id, type)
                        Log.d("track favorit? ", isFavorite.toString())

                        btn_favorite.setOnClickListener(object : View.OnClickListener{
                            override fun onClick(view: View?) {
                                checkFavorite(type, movie)
                            }
                        })

                        showLoading(false)
                        contentVisible()
                    }
                })
            }
            "tv" -> {
                supportActionBar?.setTitle(R.string.detail_tv)
                detailViewModel.getTVShow().observe(this, Observer { tvShow ->
                    if(tvShow != null) {
                        tv_title.text = tvShow.title
                        tv_overview.text = tvShow.description
                        tv_release_date.text = tvShow.releaseDate
                        rating.text = tvShow.score

                        Glide.with(applicationContext)
                            .load(BuildConfig.URL_IMAGES +tvShow.poster)
                            .apply(RequestOptions().override(350, 550))
                            .into(img_item_poster)

                        Glide.with(applicationContext)
                            .load(BuildConfig.URL_IMAGES +tvShow.backdrop)
                            .into(img_item_backdrop)

                        isFavorite = isFavorited(tvShow.id, type)

                        btn_favorite.setOnClickListener(object : View.OnClickListener{
                            override fun onClick(view: View?) {
                                checkFavoriteTV(type, tvShow)
                            }
                        })
                        showLoading(false)
                        contentVisible()
                    }
                })
            }
            "movie_favorite" -> {
                supportActionBar?.setTitle(R.string.detail_movie)
                addButton(false)
                movie = intent.getParcelableExtra("fav_movie")

                tv_title.text = movie.title
                tv_release_date.text = movie.release_date
                tv_overview.text = movie.description
                rating.text = movie.score

                Glide.with(applicationContext).load(BuildConfig.URL_IMAGES + movie.poster)
                    .into(img_item_poster)

                Glide.with(applicationContext).load(BuildConfig.URL_IMAGES + movie.backdrop)
                    .into(img_item_backdrop)

                isFavorite = isFavorited(movie.id, type)
                btn_favorite.setOnClickListener {
                    movieHelper.deleteMovie(movie.id)
                    Toast.makeText(this@DetailActivity, "${movie.title} "+ getString(R.string.tv_deleted), Toast.LENGTH_SHORT).show()

                    isFavorite = isFavorited(movie.id, type)
                    onBackPressed()
                }
                showLoading(false)
                //Tampilkan konten selesai load item selesai
                contentVisible()
            }
            "tv_favorite" -> {
                supportActionBar?.setTitle(R.string.detail_tv)
                addButton(false)
                tvShow = intent.getParcelableExtra("fav_tv")
                tv_title.text = tvShow.title
                tv_release_date.text = tvShow.releaseDate
                tv_overview.text = tvShow.description
                rating.text = tvShow.score

                Glide.with(applicationContext).load(BuildConfig.URL_IMAGES + tvShow.poster)
                    .into(img_item_poster)

                Glide.with(applicationContext).load(BuildConfig.URL_IMAGES + tvShow.backdrop)
                    .into(img_item_backdrop)

                isFavorite = isFavorited(tvShow.id, type)
                btn_favorite.setOnClickListener {
                    tvShowHelper.deleteTvShow(tvShow.id)
                    Toast.makeText(this@DetailActivity, "${tvShow.title} "+ getString(R.string.tv_deleted), Toast.LENGTH_SHORT).show()

                    isFavorite = isFavorited(tvShow.id, type)
                    onBackPressed()
                }
                showLoading(false)
                contentVisible()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar_detail.visibility = View.VISIBLE
        } else {
            progressBar_detail.visibility = View.GONE
        }
    }

    private fun isFavorited(id: String, type: String): Boolean{
        try {
            if (type == "movie" || type == "movie_favorite"){
                if (movieHelper.isMovieFavorited(id)){
                    btn_favorite.text = getString(R.string.delete)
                    addButton(false)
                    return true
                }
            }
            else if(type == "tv" || type == "tv_favorite"){
                if (tvShowHelper.isTvShowFavorited(id)){
                    btn_favorite.text = getString(R.string.delete)
                    return true
                }
            }
        } catch (e: Exception) {
            Log.d("Error", e.toString())
        }
        return false
    }

    fun checkFavorite(type: String, movie: Movie){
        when (isFavorite) {
            true -> {
                movieHelper.deleteMovie(movie.id)
                Toast.makeText(
                    this@DetailActivity, "${movie.title} " + getString(
                        R.string.delete
                    ), Toast.LENGTH_SHORT
                ).show()
                isFavorite = isFavorited(movie.id, type)
                addButton(true)
            }
            false -> {
                movieHelper.insertMovie(movie)
                Toast.makeText(this@DetailActivity, "Added ${movie.title} "+getString(
                    R.string.add_response
                ), Toast.LENGTH_SHORT).show()
                isFavorite = isFavorited(movie.id, type)
                addButton(false)
            }
        }
    }

    fun checkFavoriteTV(type: String, tvShow: TvShow){
        when (isFavorite) {
            true -> {
                Log.d("track type: ", type)
                tvShowHelper.deleteTvShow(tvShow.id)
                Toast.makeText(this@DetailActivity, "${tvShow.title} "+getString(R.string.tv_deleted), Toast.LENGTH_SHORT).show()
                isFavorite = isFavorited(tvShow.id, type)
                addButton(true)
            }
            false -> {
                tvShowHelper.insertTvShow(tvShow)
                Toast.makeText(this@DetailActivity, "Added ${tvShow.title} "+getString(
                    R.string.add_response
                ), Toast.LENGTH_SHORT).show()
                isFavorite = isFavorited(tvShow.id, type)
                addButton(false)
            }
        }
    }

    fun addButton(state: Boolean) {
        if (state){
            btn_favorite.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimaryDark))
            btn_favorite.setTextColor(Color.WHITE)
            btn_favorite.text = getString(R.string.add_to_favorite)
        } else {
            btn_favorite.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorAccent))
            btn_favorite.setTextColor(Color.WHITE)
            btn_favorite.text = getString(R.string.delete)
        }
    }

    fun contentVisible() {
        img_item_poster.visibility = View.VISIBLE
        img_item_backdrop.visibility = View.VISIBLE
        tv_title.visibility = View.VISIBLE
        star.visibility = View.VISIBLE
        overview.visibility = View.VISIBLE
        btn_favorite.visibility = View.VISIBLE
    }
}
