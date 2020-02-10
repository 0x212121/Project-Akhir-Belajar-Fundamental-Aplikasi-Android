package com.xstreamx.moviecatalogue.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import com.xstreamx.moviecatalogue.BuildConfig
import com.xstreamx.moviecatalogue.model.Movie
import com.xstreamx.moviecatalogueapi.model.TvShow
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class DetailViewModel : ViewModel() {

    private val detailMovie = MutableLiveData<Movie>()
    private val detailTVShow = MutableLiveData<TvShow>()

    lateinit var movie: Movie
    lateinit var tvShow: TvShow

    internal fun setItems(id: String?, type: String?) {

        val client = AsyncHttpClient()
        val params = RequestParams()
        params.put("api_key", BuildConfig.API_KEY)
        params.put("language", "en-US")
        when (type) {
            "movie" -> {
                val url = BuildConfig.URL_DETAIL_MOVIE+id
                Log.d("track url: ", url)
                client.get(url, params, object : JsonHttpResponseHandler() {
                    override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject?) {
                        try {
                            movie = Movie(response as JSONObject)
                            detailMovie.postValue(movie)
                        } catch (e: Exception) {
                            Log.d("Exception", e.message)
                        }
                    }
                })
            }
            "tv" -> {
                val url = BuildConfig.URL_DETAIL_TV+id
                Log.d("track url: ", url)
                client.get(url, params, object : JsonHttpResponseHandler() {
                    override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject?) {
                        try {
                            tvShow = TvShow(response as JSONObject)
                            detailTVShow.postValue(tvShow)
                        } catch (e: Exception) {
                            Log.d("Exception", e.message)
                        }
                    }
                })
            }
        }
    }

    internal fun getMovie() : MutableLiveData<Movie> {
        return detailMovie
    }

    internal fun getTVShow() : MutableLiveData<TvShow> {
        return detailTVShow
    }
}