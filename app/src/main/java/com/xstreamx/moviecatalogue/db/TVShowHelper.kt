package com.xstreamx.moviecatalogue.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.xstreamx.moviecatalogue.db.DatabaseContract.MovieColumns.Companion.BACKDROP
import com.xstreamx.moviecatalogue.db.DatabaseContract.MovieColumns.Companion.DESCRIPTION
import com.xstreamx.moviecatalogue.db.DatabaseContract.MovieColumns.Companion.ID
import com.xstreamx.moviecatalogue.db.DatabaseContract.MovieColumns.Companion.POSTER
import com.xstreamx.moviecatalogue.db.DatabaseContract.MovieColumns.Companion.SCORE
import com.xstreamx.moviecatalogue.db.DatabaseContract.MovieColumns.Companion.TITLE
import com.xstreamx.moviecatalogue.db.DatabaseContract.TVShowColumns.Companion.FIRST_AIR_DATE
import com.xstreamx.moviecatalogueapi.model.TvShow
import java.sql.SQLException


class TVShowHelper(context: Context) {
    val DATABASE_TABLE = DatabaseContract().TABLE_TV_SHOW
    var databaseHelper = DatabaseHelper(context)
    lateinit var database: SQLiteDatabase

    companion object {
        @Volatile private var INSTANCE: TVShowHelper? = null

        fun getInstance(context: Context): TVShowHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: TVShowHelper(context)
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

    fun getAllTVShow(): ArrayList<TvShow> {
        val arrayList = ArrayList<TvShow>()
        val cursor = database.query(
            DATABASE_TABLE,
            null, null, null, null, null,
            null, null
        )
        cursor.moveToFirst()
        var tvShow: TvShow
        if (cursor.count > 0) {
            do {
                tvShow = TvShow(
                    cursor.getString(cursor.getColumnIndexOrThrow(ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FIRST_AIR_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SCORE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(POSTER)),
                    cursor.getString(cursor.getColumnIndexOrThrow(BACKDROP))
                )

                arrayList.add(tvShow)
                cursor.moveToNext()

            } while (!cursor.isAfterLast)
        }
        cursor.close()
        return arrayList
    }

    fun isTvShowFavorited(id: String): Boolean {
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

    fun insertTvShow(tvShow: TvShow): Long {
        val args = ContentValues()
        args.put(ID, tvShow.id)
        args.put(TITLE, tvShow.title)
        args.put(FIRST_AIR_DATE, tvShow.releaseDate)
        args.put(SCORE, tvShow.score)
        args.put(DESCRIPTION, tvShow.description)
        args.put(POSTER, tvShow.poster)
        args.put(BACKDROP, tvShow.backdrop)
        return database.insert(DATABASE_TABLE, null, args)
    }

    fun deleteTvShow(id: String): Int {
        return database.delete(DatabaseContract().TABLE_TV_SHOW, "$ID = '$id'", null)
    }

}

