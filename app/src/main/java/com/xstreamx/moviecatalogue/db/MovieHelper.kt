package com.xstreamx.moviecatalogue.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.xstreamx.moviecatalogue.db.DatabaseContract.MovieColumns.Companion.BACKDROP
import com.xstreamx.moviecatalogue.db.DatabaseContract.MovieColumns.Companion.DESCRIPTION
import com.xstreamx.moviecatalogue.db.DatabaseContract.MovieColumns.Companion.ID
import com.xstreamx.moviecatalogue.db.DatabaseContract.MovieColumns.Companion.POSTER
import com.xstreamx.moviecatalogue.db.DatabaseContract.MovieColumns.Companion.RELEASE_DATE
import com.xstreamx.moviecatalogue.db.DatabaseContract.MovieColumns.Companion.SCORE
import com.xstreamx.moviecatalogue.db.DatabaseContract.MovieColumns.Companion.TITLE
import com.xstreamx.moviecatalogue.model.Movie
import java.sql.SQLException


class MovieHelper(context: Context) {
    val DATABASE_TABLE = DatabaseContract().TABLE_MOVIE
    var databaseHelper = DatabaseHelper(context)
    lateinit var database: SQLiteDatabase

    companion object {

        @Volatile private var INSTANCE: MovieHelper? = null

        fun getInstance(context: Context): MovieHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: MovieHelper(context)
            }

    }

    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()

        if (database.isOpen())
            database.close()
    }

    fun getAllMovie(): ArrayList<Movie> {
        val arrayList = ArrayList<Movie>()
        val cursor = database.query(
            DATABASE_TABLE,
            null, null, null, null, null,
            null, null
        )
        cursor.moveToFirst()
        var movie: Movie
        if (cursor.count > 0) {
            do {
                movie = Movie(
                    cursor.getString(cursor.getColumnIndexOrThrow(ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SCORE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(POSTER)),
                    cursor.getString(cursor.getColumnIndexOrThrow(BACKDROP))
                )

                arrayList.add(movie)
                cursor.moveToNext()

            } while (!cursor.isAfterLast)
        }
        cursor.close()
        return arrayList
    }

    fun isMovieFavorited(id: String): Boolean {
        val cursor = database.query(
            DATABASE_TABLE,
            null, "$ID = '$id'", null, null, null,
            null, null
        )
        cursor.moveToFirst()
        if (cursor.count > 0) {
            return true
        }
        return false
    }

    fun insertMovie(movie: Movie): Long {
        val args = ContentValues()
        args.put(ID, movie.id)
        args.put(TITLE, movie.title)
        args.put(RELEASE_DATE, movie.release_date)
        args.put(SCORE, movie.score)
        args.put(DESCRIPTION, movie.description)
        args.put(POSTER, movie.poster)
        args.put(BACKDROP, movie.backdrop)
        return database.insert(DATABASE_TABLE, null, args)
    }

    fun deleteMovie(id: String): Int {
        return database.delete(DatabaseContract().TABLE_MOVIE, "$ID = '$id'", null)
    }

    fun queryByIdProvider(id: String): Cursor {
        return database.query(DATABASE_TABLE, null, "$ID = ?", arrayOf(id), null,
            null, null, null)
    }

    fun queryProvider(): Cursor {
        return database.query(DATABASE_TABLE, null, null, null, null,
            null, "$ID ASC")
    }

    fun insertProvider(values: ContentValues): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun updateProvider(id: String, values: ContentValues): Int {
        return database.update(DATABASE_TABLE, values, "$ID = ?", arrayOf(id))
    }

    fun deleteProvider(id: String): Int {
        return database.delete(DATABASE_TABLE, "$ID = ?", arrayOf(id))
    }
}

