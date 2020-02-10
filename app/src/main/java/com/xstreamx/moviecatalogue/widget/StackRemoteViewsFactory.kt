package com.xstreamx.moviecatalogue.widget

import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.xstreamx.moviecatalogue.BuildConfig
import com.xstreamx.moviecatalogue.R
import com.xstreamx.moviecatalogue.db.MovieHelper
import com.xstreamx.moviecatalogue.model.Movie

class StackRemoteViewsFactory(val context: Context) : RemoteViewsService.RemoteViewsFactory {

    private val mWidgetItems = ArrayList<Movie>()
    lateinit var movieHelper: MovieHelper

    override fun onCreate() {

    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun onDataSetChanged() {
        val identityToken = Binder.clearCallingIdentity()
        movieHelper = MovieHelper.getInstance(context)
        movieHelper.open()
        mWidgetItems.clear()
        mWidgetItems.addAll(movieHelper.getAllMovie())

        Binder.restoreCallingIdentity(identityToken)
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.widget_item)
        var poster = Glide.with(context)
            .asBitmap()
            .load(BuildConfig.URL_IMAGES + mWidgetItems[position].poster)
            .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            .get()

        rv.setImageViewBitmap(R.id.imageView, poster)
        val extras = Bundle()
        extras.putInt(FavoriteMovieWidget().EXTRA_ITEM, position)
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getCount(): Int {
        return mWidgetItems.size
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun onDestroy() {

    }
}