package com.andriawan.moviedb.ui.main

import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.GridLayoutManager
import com.andriawan.moviedb.adapter.MovieListAdapter
import com.andriawan.moviedb.base.BaseActivity
import com.andriawan.moviedb.databinding.ActivityMainBinding
import com.andriawan.moviedb.utils.decorator.GridSpacerDecorator
import com.andriawan.moviedb.utils.extensions.hideKeyboard

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val movieListAdapter = MovieListAdapter()

    override val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initUI() {
        setupSystemPadding(isVerticalEnabled = true)

        binding.apply {
            etSearch.setOnEditorActionListener { v, eventId, _ ->
                if (eventId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard()
                    v.clearFocus()

                    // TODO: Implement search function
                }

                true
            }

            rvMovies.apply {
                adapter = movieListAdapter
                layoutManager = GridLayoutManager(this@MainActivity, 2)
                addItemDecoration(GridSpacerDecorator(2))
            }
        }
    }
}