package com.xstreamx.moviecatalogue.db

import android.net.Uri
import android.provider.BaseColumns

class DatabaseContract {

    val AUTHORITY = "com.xstreamx.moviecatalogue"
    val SCHEME = "content"

    var TABLE_MOVIE = "movie"
    var TABLE_TV_SHOW = "tv_show"

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

    internal class TVShowColumns : BaseColumns {
        companion object {
            var ID = "id"
            var TITLE = "title"
            var FIRST_AIR_DATE ="first_air_date"
            var SCORE = "score"
            var DESCRIPTION = "description"
            var POSTER = "poster"
            var BACKDROP = "backdrop"
        }
    }
}