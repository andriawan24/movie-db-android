package com.andriawan.moviedb.ui.main

import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.andriawan.moviedb.R
import com.andriawan.moviedb.adapter.MovieListAdapter
import com.andriawan.moviedb.adapter.MovieListAdapter.Companion.VIEW_TYPE_LOADING
import com.andriawan.moviedb.adapter.MovieLoadStateAdapter
import com.andriawan.moviedb.base.BaseActivity
import com.andriawan.moviedb.databinding.ActivityMainBinding
import com.andriawan.moviedb.ui.components.ErrorBottomSheet
import com.andriawan.moviedb.utils.decorator.GridSpacerDecorator
import com.andriawan.moviedb.utils.extensions.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okio.IOException
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val mainViewModel: MainViewModel by viewModels()

    private val movieListAdapter = MovieListAdapter()

    override val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initUI() {
        setupSystemPadding(isVerticalEnabled = true)
        setupRecyclerView()
        observeViewModel()
    }

    override fun initListener() {
        binding.apply {
            etSearch.setOnEditorActionListener { v, eventId, _ ->
                if (eventId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard()
                    v.clearFocus()

                    val query = etSearch.text.toString().trim()
                    mainViewModel.searchMovies(query = query)
                }
                true
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvMovies.apply {
            adapter = movieListAdapter
                .withLoadStateFooter(MovieLoadStateAdapter(movieListAdapter::retry))
            val gridLayoutManager = GridLayoutManager(this@MainActivity, 2)
            layoutManager = gridLayoutManager
            addItemDecoration(GridSpacerDecorator(2))
            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val viewType = movieListAdapter.getItemViewType(position)
                    return if (viewType == VIEW_TYPE_LOADING) 2 else 1
                }
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            mainViewModel.movies.collectLatest { pagingData ->
                movieListAdapter.submitData(pagingData)
            }
        }

        lifecycleScope.launch {
            movieListAdapter.loadStateFlow.collectLatest { loadState ->
                val refresh = loadState.refresh

                binding.layoutEmptyMovieList.isVisible = false

                if (refresh is LoadState.NotLoading && movieListAdapter.itemCount == 0) {
                    binding.layoutEmptyMovieList.isVisible = true
                }

                if (refresh is LoadState.Error) {
                    refresh.error.let {
                        Timber.e("Error occurred: ${it.message}")
                        binding.layoutEmptyMovieList.isVisible = true

                        when (it) {
                            is IOException -> {
                                ErrorBottomSheet(getString(R.string.error_no_internet)).show(
                                    supportFragmentManager,
                                    ErrorBottomSheet.TAG
                                )
                            }

                            // TODO: Handling other errors

                            else -> {
                                ErrorBottomSheet(getString(R.string.error_unknown)).show(
                                    supportFragmentManager,
                                    ErrorBottomSheet.TAG
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}