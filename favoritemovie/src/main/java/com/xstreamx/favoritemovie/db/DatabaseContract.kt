package com.xstreamx.favoritemovie.db

import android.net.Uri
import android.provider.BaseColumns

class DatabaseContract {
    val AUTHORITY = "com.xstreamx.moviecatalogue"
    val SCHEME = "content"

    var TABLE_MOVIE = "movie"

    internal class MovieColumns : BaseColumns {
        companion object {
            var ID = "id"
            var TITLE = "title"
            var RELEASE_DATE = "release_date"
            var SCORE = "score"
            var DESCRIPTION = "description"
            var POSTER = "poster"
            var BACKDROP = "backdrop"

            val CONTENT_URI = Uri.Builder().scheme(DatabaseContract().SCHEME)
                .authority(DatabaseContract().AUTHORITY)
                .appendPath(DatabaseContract().TABLE_MOVIE)
                .build()
        }
    }
}