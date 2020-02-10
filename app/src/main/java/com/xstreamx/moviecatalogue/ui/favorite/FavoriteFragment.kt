package com.xstreamx.moviecatalogue.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.xstreamx.moviecatalogue.R
import com.xstreamx.moviecatalogueapi.adapter.TabAdapter
import kotlinx.android.synthetic.main.fragment_favorites.*

class FavoriteFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sectionsPagerAdapter = TabAdapter(requireContext(), childFragmentManager)
        view_pager_favorite.adapter = sectionsPagerAdapter
        tab_layout_favorite.setupWithViewPager(view_pager_favorite)
    }
}