package com.andriawan.moviedb.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andriawan.moviedb.databinding.ItemActorBinding
import com.andriawan.moviedb.databinding.ItemCategoryBinding
import com.andriawan.moviedb.databinding.ItemMovieBinding
import com.andriawan.moviedb.ui.detail.DetailActivity
import com.andriawan.moviedb.utils.extensions.px
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryBinding.inflate(
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
        private val binding: ItemCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            binding.tvCategoryName.text = "Drama"

            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, DetailActivity::class.java)
                binding.root.context.startActivity(intent)
            }
        }
    }
}