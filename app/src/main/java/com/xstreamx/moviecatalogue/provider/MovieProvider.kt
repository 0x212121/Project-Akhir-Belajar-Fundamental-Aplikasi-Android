package com.xstreamx.moviecatalogue.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.xstreamx.moviecatalogue.db.DatabaseContract
import com.xstreamx.moviecatalogue.db.DatabaseContract.MovieColumns.Companion.CONTENT_URI
import com.xstreamx.moviecatalogue.db.DatabaseContract.MovieColumns.Companion.ID
import com.xstreamx.moviecatalogue.db.MovieHelper

class MovieProvider : ContentProvider() {

    companion object {
        private val MOVIE = 1
        private val MOVIE_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var movieHelper: MovieHelper
    }

    override fun onCreate(): Boolean {
        uriMatching()
        movieHelper = MovieHelper.getInstance(context as Context)

        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        movieHelper.open()
        return when (sUriMatcher.match(uri)) {
            MOVIE -> movieHelper.queryProvider()
            MOVIE_ID -> movieHelper.queryByIdProvider(uri.lastPathSegment as String)
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        movieHelper.open()
        val added: Long
        when (sUriMatcher.match(uri)) {
            MOVIE -> added = movieHelper.insertProvider(values as ContentValues)
            else -> added = 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse(CONTENT_URI.toString()+ "/" + added)
    }

    override fun update(uri: Uri, values: ContentValues?, s: String?, strings: Array<String>?): Int {
        val updated: Int = when (MOVIE_ID) {
            sUriMatcher.match(uri) -> movieHelper.updateProvider(uri.lastPathSegment.toString(), values!!)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return updated
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (MOVIE_ID) {
            sUriMatcher.match(uri) -> movieHelper.deleteProvider(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }

    private fun uriMatching() {
        sUriMatcher.addURI(DatabaseContract().AUTHORITY, DatabaseContract().TABLE_MOVIE, MOVIE)
        sUriMatcher.addURI(DatabaseContract().AUTHORITY, DatabaseContract().TABLE_MOVIE + "/$ID", MOVIE_ID)
    }
}
