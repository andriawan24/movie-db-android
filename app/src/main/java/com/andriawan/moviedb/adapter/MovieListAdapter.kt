package com.andriawan.moviedb.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andriawan.moviedb.databinding.ItemMovieBinding
import com.andriawan.moviedb.ui.detail.DetailActivity
import com.andriawan.moviedb.utils.extensions.px
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class MovieListAdapter : RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = 10

    inner class ViewHolder(
        private val binding: ItemMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            Glide.with(binding.root.context)
                .load("https://media.themoviedb.org/t/p/w440_and_h660_face/xdzLBZjCVSEsic7m7nJc4jNJZVW.jpg")
                .apply(
                    RequestOptions.bitmapTransform(RoundedCorners(8.px))
                )
                .into(binding.ivPoster)

            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, DetailActivity::class.java)
                binding.root.context.startActivity(intent)
            }
        }
    }
}