package com.xstreamx.moviecatalogue.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.xstreamx.moviecatalogue.BuildConfig
import com.xstreamx.moviecatalogue.R
import com.xstreamx.moviecatalogue.model.Movie
import com.xstreamx.moviecatalogue.ui.DetailActivity
import com.xstreamx.moviecatalogue.ui.MainActivity.Companion.EXTRA_ID
import com.xstreamx.moviecatalogue.ui.MainActivity.Companion.EXTRA_TYPE
import kotlinx.android.synthetic.main.item_list.view.*

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    private val mData = ArrayList<Movie>()

    fun setData(items: ArrayList<Movie>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            with(itemView){
                tv_title_item.text = movie.title
                tv_vote_average_item.text = movie.score
                tv_release_date_value.text = movie.release_date
                tv_overview_value.text = movie.description

                Glide.with(itemView.context)
                    .load(BuildConfig.URL_IMAGES+movie.poster)
                    .apply(RequestOptions().override(350, 550))
                    .into(image_poster_item)

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra(EXTRA_ID, movie.id)
                    intent.putExtra(EXTRA_TYPE, "movie")
                    context.startActivity(intent)
                }
            }
        }
    }
}