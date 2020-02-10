package com.xstreamx.favoritemovie

import android.content.Intent
import android.database.ContentObserver
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.xstreamx.favoritemovie.adapter.FavoriteMovieAdapter
import com.xstreamx.favoritemovie.db.DatabaseContract.MovieColumns.Companion.CONTENT_URI
import com.xstreamx.favoritemovie.helper.MappingHelper
import com.xstreamx.favoritemovie.model.Movie
import kotlinx.android.synthetic.main.activity_main.*
import java.text.FieldPosition

class MainActivity : AppCompatActivity() {

    lateinit var favoriteMovies: ArrayList<Movie>

    private var handlerThread: HandlerThread? = null
    private var myObserver: DataObserver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Favorite Movies"

        handlerThread = HandlerThread("DataObserver")
        handlerThread?.start()
        val handler = Handler(handlerThread?.looper)
        myObserver = DataObserver(handler)
        contentResolver?.registerContentObserver(CONTENT_URI, true, myObserver as ContentObserver)

        val list = contentResolver?.query(CONTENT_URI, null,
            null, null, null
        )

        favoriteMovies = MappingHelper().mapCursorToArrayList(list as Cursor)

        rv_favorite_movie.apply {

//            adapter = FavoriteMovieAdapter(favoriteMovies, context, object : FavoriteMovieAdapter.onItemClicked {
//                override fun onItemClick(position: Int) {
//                    val intent = Intent(context, DetailActivity::class.java)
//                    intent.putExtra("extra_type", "movie_favorite")
//                    intent.putExtra("extra_id", favoriteMovies[position].id)
//                    intent.putExtra("fav_movie", favoriteMovies[position])
//                    startActivity(intent)
//                }
//            })
            layoutManager = LinearLayoutManager(context)
        }

        rv_favorite_movie.setOnClickListener {

        }
    }

    class DataObserver(handler: Handler) : ContentObserver(handler)
}
