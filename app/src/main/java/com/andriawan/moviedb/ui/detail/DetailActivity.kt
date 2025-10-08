package com.andriawan.moviedb.ui.detail

import android.content.Intent
import android.graphics.Color
import android.view.ViewGroup
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateMargins
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andriawan.moviedb.R
import com.andriawan.moviedb.adapter.ActorAdapter
import com.andriawan.moviedb.adapter.CategoryAdapter
import com.andriawan.moviedb.base.BaseActivity
import com.andriawan.moviedb.databinding.ActivityDetailBinding
import com.andriawan.moviedb.domain.models.MovieDetail
import com.andriawan.moviedb.ui.components.ErrorBottomSheet
import com.andriawan.moviedb.utils.Constants.IMAGE_BASE_URL
import com.andriawan.moviedb.utils.extensions.extractYear
import com.andriawan.moviedb.utils.extensions.getRuntimeFormatted
import com.andriawan.moviedb.utils.extensions.loadImage
import com.andriawan.moviedb.utils.extensions.observe
import com.andriawan.moviedb.utils.extensions.px
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class DetailActivity : BaseActivity<ActivityDetailBinding>() {

    private val actorAdapter = ActorAdapter()
    private val categoryAdapter = CategoryAdapter()
    private var movieId = -1
    private val detailViewModel: DetailViewModel by viewModels()

    override val binding: ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    override fun initIntent() {
        super.initIntent()
        movieId = intent.getIntExtra(EXTRA_MOVIE_ID, -1)
    }

    override fun initUI() {
        initObserver()
        initLayoutPadding()
        initAdapter()
    }

    private fun initObserver() {
        lifecycleScope.launch {
            detailViewModel.getMovieDetail(movieId).observe(
                onLoading = {
                    Timber.d("Loading movie")
                },
                onSuccess = { movie ->
                    loadPosterImage(movie.posterPath)
                    updateUi(movie)
                    setupTrailerButtonListener(movie)
                },
                onError = { error ->
                    Timber.d("Error get detail movie: ${error.message}")
                    ErrorBottomSheet(
                        message = error.message
                    ).show(supportFragmentManager, ErrorBottomSheet.TAG)
                }
            )
        }
    }

    private fun loadPosterImage(posterPath: String) {
        binding.ivPoster.loadImage(IMAGE_BASE_URL + posterPath)
    }

    private fun setupTrailerButtonListener(movie: MovieDetail) {
        binding.apply {
            btnSearchTrailer.setOnClickListener {
                val searchQuery = "Trailer ${movie.title}"
                val url = "https://www.youtube.com/results?search_query=$searchQuery"
                val intent = Intent(Intent.ACTION_VIEW, url.toUri())

                try {
                    startActivity(intent)
                } catch (_: Exception) {
                    ErrorBottomSheet("No app found to handle YouTube search.")
                        .show(supportFragmentManager, ErrorBottomSheet.TAG)
                }
            }
        }
    }

    private fun updateUi(movie: MovieDetail) {
        binding.apply {
            tvTitle.text = movie.title
            tvDescription.text = movie.overview
            tvRating.text = getString(
                R.string.label_rating,
                movie.voteAverage
            )
            tvMeta.text = getString(
                R.string.label_meta,
                movie.releaseDate.extractYear(),
                movie.productionCountries.firstOrNull()?.iso31661 ?: "-",
                movie.runtime.getRuntimeFormatted()
            )

            categoryAdapter.submitList(movie.genres)

            observeMovieCredits(movie.id)
        }
    }

    private fun observeMovieCredits(movieId: Int) {
        lifecycleScope.launch {
            detailViewModel.getMovieCredits(movieId).observe(
                onLoading = {

                },
                onSuccess = {
                    actorAdapter.submitList(it.cast)
                },
                onError = {

                }
            )
        }
    }

    override fun initListener() {
        super.initListener()
        binding.containerBackButton.setOnClickListener {
            finish()
        }
    }

    private fun initAdapter() {
        binding.rvActors.apply {
            layoutManager = LinearLayoutManager(this@DetailActivity, RecyclerView.HORIZONTAL, false)
            adapter = actorAdapter
        }

        binding.rvCategories.apply {
            layoutManager = LinearLayoutManager(this@DetailActivity, RecyclerView.HORIZONTAL, false)
            adapter = categoryAdapter
        }
    }

    private fun initLayoutPadding() {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )
        setupSystemPadding(isBottomEnabled = true)
        setupBackButtonMarginTop()
    }

    private fun setupBackButtonMarginTop() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.containerBackButton) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val layoutParams = v.layoutParams as? ViewGroup.MarginLayoutParams
            layoutParams?.updateMargins(top = systemBars.top + 16.px)
            insets
        }
    }

    companion object {
        const val EXTRA_MOVIE_ID = "EXTRA_MOVIE_ID"
    }
}