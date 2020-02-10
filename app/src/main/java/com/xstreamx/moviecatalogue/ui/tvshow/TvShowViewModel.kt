package com.xstreamx.moviecatalogue.ui.tvshow

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import com.xstreamx.moviecatalogue.BuildConfig
import com.xstreamx.moviecatalogueapi.model.TvShow
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class TvShowViewModel : ViewModel() {

    private val listTVShows = MutableLiveData<ArrayList<TvShow>>()

    internal fun setTVShows() {
        val listItemsTvShow = ArrayList<TvShow>()

        val params = RequestParams()
        params.put("api_key", BuildConfig.API_KEY)
        params.put("language", "en-US")
        val client = AsyncHttpClient()
        val url = "https://api.themoviedb.org/3/discover/tv"

        client.get(url, params, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("results")
                    for (i in 0 until list.length()) {
                        val tvShow = list.getJSONObject(i)
                        val tvShowItems = TvShow(tvShow)
                        listItemsTvShow.add(tvShowItems)
                    }
                    listTVShows.postValue(listItemsTvShow)

                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }

    internal fun getTVShows() : LiveData<ArrayList<TvShow>> {
        return listTVShows
    }

    internal fun searchTVShows(type: String, query: String) {
        val listItemsTVShow = ArrayList<TvShow>()
        Log.d("track", query)

        val params = RequestParams()
        params.put("api_key", BuildConfig.API_KEY)
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
                        if (type == "tv"){
                            Log.d("track: ", type)
                            listTVShows.postValue(null)
                        }
                    }
                    else{
                        for (i in 0 until list.length()) {
                            if (type == "tv"){
                                val tvShow = list.getJSONObject(i)
                                val tvShowItems = TvShow(tvShow)
                                listItemsTVShow.add(tvShowItems)
                                listTVShows.postValue(listItemsTVShow)

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