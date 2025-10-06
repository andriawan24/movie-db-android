package com.andriawan24.moviedb.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andriawan24.moviedb.databinding.ItemMovieBinding
import com.bumptech.glide.Glide

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
                .into(binding.ivPoster)
        }
    }
}