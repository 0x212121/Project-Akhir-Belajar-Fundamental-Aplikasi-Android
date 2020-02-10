package com.xstreamx.moviecatalogue.ui.favorite


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xstreamx.moviecatalogue.ui.DetailActivity
import com.xstreamx.moviecatalogue.R
import com.xstreamx.moviecatalogue.adapter.FavoriteTVShowAdapter
import com.xstreamx.moviecatalogue.db.TVShowHelper
import com.xstreamx.moviecatalogueapi.model.TvShow
import kotlinx.android.synthetic.main.fragment_favorite_tvshow.*

class FavoriteTVShowFragment : Fragment() {

    lateinit var tvShowHelper: TVShowHelper
    lateinit var favoriteTVShow: ArrayList<TvShow>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite_tvshow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvShowHelper = TVShowHelper.getInstance(context!!)
        tvShowHelper.open()

        favoriteTVShow = tvShowHelper.getAllTVShow()

        val reverseLinearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,
            true)
        rv_favorite_tv_show.apply {
            adapter = FavoriteTVShowAdapter(favoriteTVShow, context, object : FavoriteTVShowAdapter.OnItemClicked {
                override fun onItemClick(position: Int) {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra("extra_type", "tv_favorite")
                    intent.putExtra("extra_id", favoriteTVShow[position].id)
                    intent.putExtra("fav_tv", favoriteTVShow[position])
                    startActivity(intent)
                }
            })
            reverseLinearLayoutManager.stackFromEnd = true
            layoutManager = reverseLinearLayoutManager
        }
        progressBar_favorite_tv_show.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        tvShowHelper.open()
        favoriteTVShow.clear()
        favoriteTVShow.addAll(tvShowHelper.getAllTVShow())
        rv_favorite_tv_show.adapter?.notifyDataSetChanged()
    }
}
