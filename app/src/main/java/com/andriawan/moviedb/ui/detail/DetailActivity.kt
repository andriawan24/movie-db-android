package com.andriawan.moviedb.ui.detail

import android.content.Intent
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
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
                    startShimmerDetail()
                },
                onSuccess = { movie ->
                    loadPosterImage(movie.posterPath)
                    updateUi(movie)
                    setupTrailerButtonListener(movie)
                },
                onError = { error ->
                    stopShimmerDetail()
                    ErrorBottomSheet(error.message).show(
                        supportFragmentManager,
                        ErrorBottomSheet.TAG
                    )
                }
            )
        }
    }

    private fun startShimmerDetail() {
        binding.apply {
            layoutShimmerDetail.root.apply {
                startShimmer()
                visibility = View.VISIBLE
            }

            layoutPosterShimmer.apply {
                startShimmer()
                visibility = View.VISIBLE
            }

            layoutMainDetail.isVisible = false
        }
    }

    private fun stopShimmerDetail() {
        binding.apply {
            layoutShimmerDetail.root.apply {
                stopShimmer()
                visibility = View.GONE
            }

            layoutPosterShimmer.apply {
                stopShimmer()
                visibility = View.VISIBLE
            }

            layoutMainDetail.isVisible = true
        }
    }

    private fun startShimmerActor() {
        binding.apply {
            layoutActorLoading.apply {
                startShimmer()
                visibility = View.VISIBLE
            }

            rvActors.isVisible = false
        }
    }

    private fun stopShimmerActor() {
        binding.apply {
            layoutActorLoading.apply {
                stopShimmer()
                visibility = View.GONE
            }

            rvActors.isVisible = true
        }
    }

    private fun loadPosterImage(posterPath: String) {
        binding.ivPoster.loadImage(IMAGE_BASE_URL + posterPath)
    }

    private fun setupTrailerButtonListener(movie: MovieDetail) {
        binding.apply {
            btnSearchTrailer.setOnClickListener {
                val searchQuery = getString(R.string.template_trailer_youtube_search, movie.title)
                val url = "https://www.youtube.com/results?search_query=$searchQuery"
                val intent = Intent(Intent.ACTION_VIEW, url.toUri())

                try {
                    startActivity(intent)
                } catch (_: Exception) {
                    ErrorBottomSheet(getString(R.string.error_youtube_not_found))
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
            stopShimmerDetail()
        }
    }

    private fun observeMovieCredits(movieId: Int) {
        lifecycleScope.launch {
            detailViewModel.getMovieCredits(movieId).observe(
                onLoading = {
                    startShimmerActor()
                },
                onSuccess = {
                    actorAdapter.submitList(it.cast)
                    stopShimmerActor()
                },
                onError = {
                    stopShimmerActor()
                    binding.apply {
                        rvActors.isVisible = false
                        tvTitleActor.isVisible = false
                    }
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
        binding.apply {
            rvActors.apply {
                layoutManager = LinearLayoutManager(
                    this@DetailActivity,
                    RecyclerView.HORIZONTAL,
                    false
                )

                adapter = actorAdapter
            }

            rvCategories.apply {
                layoutManager = LinearLayoutManager(
                    this@DetailActivity,
                    RecyclerView.HORIZONTAL,
                    false
                )

                adapter = categoryAdapter
            }
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