package com.andriawan24.moviedb.ui.main

import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.GridLayoutManager
import com.andriawan24.moviedb.adapter.MovieListAdapter
import com.andriawan24.moviedb.base.BaseActivity
import com.andriawan24.moviedb.databinding.ActivityMainBinding
import com.andriawan24.moviedb.utils.decorator.GridSpacerDecorator
import com.andriawan24.moviedb.utils.extensions.hideKeyboard

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val movieListAdapter = MovieListAdapter()

    override val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initUI() {
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