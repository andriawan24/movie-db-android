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
import com.bumptech.glide.Glide

class DetailActivity : BaseActivity<ActivityDetailBinding>() {

    private val actorAdapter = ActorAdapter()
    private val categoryAdapter = CategoryAdapter()

    override val binding: ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    override fun initUI() {
        initLayoutPadding()

        initAdapter()

        Glide.with(binding.root.context)
            .load("https://media.themoviedb.org/t/p/w440_and_h660_face/xdzLBZjCVSEsic7m7nJc4jNJZVW.jpg")
            .into(binding.ivPoster)
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
        setupSystemPadding()
        ViewCompat.setOnApplyWindowInsetsListener(binding.containerBackButton) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val layoutParams = v.layoutParams as? ViewGroup.MarginLayoutParams
            layoutParams?.updateMargins(top = systemBars.top)
            insets
        }
    }
}