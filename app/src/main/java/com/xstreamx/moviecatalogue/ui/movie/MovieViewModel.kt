package com.xstreamx.moviecatalogue.ui.movie

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import com.xstreamx.moviecatalogue.BuildConfig
import com.xstreamx.moviecatalogue.BuildConfig.API_KEY
import com.xstreamx.moviecatalogue.model.Movie
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MovieViewModel : ViewModel() {

    private val listMovies = MutableLiveData<ArrayList<Movie>>()

    internal fun setMovies() {
        val listItemsMovie = ArrayList<Movie>()

        val params = RequestParams()
        params.put("api_key", API_KEY)
        params.put("language", "en-US")
        val client = AsyncHttpClient()
        val url = "https://api.themoviedb.org/3/discover/movie"

        client.get(url, params, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("results")
                    for (i in 0 until list.length()) {
                        val movie = list.getJSONObject(i)
                        val movieItems = Movie(movie)
                        listItemsMovie.add(movieItems)
                    }
                    listMovies.postValue(listItemsMovie)
                    Log.d("track lists: ", listMovies.toString())

                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }

    internal fun getMovies() : LiveData<ArrayList<Movie>> {
        return listMovies
    }

    internal fun searchMoviesTVShows(type: String, query: String) {
        val listItemsMovie = ArrayList<Movie>()
        Log.d("track", query)

        val params = RequestParams()
        params.put("api_key", API_KEY)
        params.put("language", "en-US")
        params.put("query", query)
        val client = AsyncHttpClient()
        val url = BuildConfig.URL_SEARCH+"$type"
        Log.d("track query: ", url)
        client.get(url, params, object : AsyncHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("results")
                    if (list.length() < 1){
                        if (type == "movie"){
                            Log.d("track not found: ", type)
                            listMovies.postValue(null)
                        }
                    }
                    else{
                        for (i in 0 until list.length()) {
                            if (type == "movie"){
                                val movie = list.getJSONObject(i)
                                val movieItems = Movie(movie)
                                listItemsMovie.add(movieItems)
                                listMovies.postValue(listItemsMovie)
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.d("Exception", e.message)
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                Log.d("onFailure", error.message)
            }
        })
    }
}