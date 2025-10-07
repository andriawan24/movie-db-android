package com.andriawan.moviedb.ui.detail

import android.graphics.Color
import android.view.ViewGroup
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateMargins
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andriawan.moviedb.adapter.ActorAdapter
import com.andriawan.moviedb.adapter.CategoryAdapter
import com.andriawan.moviedb.base.BaseActivity
import com.andriawan.moviedb.databinding.ActivityDetailBinding
import com.andriawan.moviedb.utils.extensions.px
import com.bumptech.glide.Glide
import timber.log.Timber

class DetailActivity : BaseActivity<ActivityDetailBinding>() {

    private val actorAdapter = ActorAdapter()
    private val categoryAdapter = CategoryAdapter()

    override val binding: ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    override fun initIntent() {
        super.initIntent()
        val movieId = intent.getIntExtra(EXTRA_MOVIE_ID, -1)
        Timber.d("Movie ID: $movieId")
    }

    override fun initUI() {
        initLayoutPadding()
        initAdapter()
        Glide.with(binding.root.context)
            .load("https://media.themoviedb.org/t/p/w440_and_h660_face/xdzLBZjCVSEsic7m7nJc4jNJZVW.jpg")
            .into(binding.ivPoster)
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