package com.xstreamx.moviecatalogue.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xstreamx.moviecatalogue.BuildConfig
import com.xstreamx.moviecatalogue.R
import com.xstreamx.moviecatalogue.model.Movie

class FavoriteMovieAdapter(private val listItems: ArrayList<Movie>, private val context: Context,
                           private val mListener: OnItemClicked)  : RecyclerView.Adapter<FavoriteMovieAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listItems[position]
        holder.tvTitle.text = item.title
        holder.tvRating.text = item.score
        holder.tvYear.text = item.release_date
        holder.overview.text = item.description
        Glide.with(context).load(BuildConfig.URL_IMAGES+item.poster).into(holder.imgPoster)
        holder.cardView.setOnClickListener {
            mListener.onItemClick(position)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title_item)
        val tvRating: TextView = itemView.findViewById(R.id.tv_vote_average_item)
        val tvYear: TextView = itemView.findViewById(R.id.tv_release_date_value)
        val imgPoster: ImageView = itemView.findViewById(R.id.image_poster_item)
        val overview: TextView = itemView.findViewById(R.id.tv_overview_value)
        val cardView: CardView = itemView.findViewById(R.id.card_view)
    }

    interface OnItemClicked {
        fun onItemClick(position: Int)
    }
}