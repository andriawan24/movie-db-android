package com.andriawan.moviedb.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.andriawan.moviedb.databinding.ItemMovieBinding
import com.andriawan.moviedb.domain.models.Movie
import com.andriawan.moviedb.ui.detail.DetailActivity
import com.andriawan.moviedb.utils.Constants.IMAGE_BASE_URL
import com.andriawan.moviedb.utils.extensions.extractYear
import com.andriawan.moviedb.utils.extensions.loadImageRounded

class MovieListAdapter : PagingDataAdapter<Movie, MovieListAdapter.ViewHolder>(COMPARATOR) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        getItem(position)?.let { movie ->
            holder.bind(movie)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount) {
            VIEW_TYPE_LOADING
        } else {
            VIEW_TYPE_MOVIE
        }
    }

    inner class ViewHolder(
        val binding: ItemMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.apply {
                tvTitle.text = movie.title
                tvYear.text = movie.releaseDate.extractYear()

                movie.posterPath.let { path ->
                    ivPoster.loadImageRounded(IMAGE_BASE_URL + path)
                }

                root.setOnClickListener {
                    val intent = Intent(root.context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_MOVIE_ID, movie.id)
                    root.context.startActivity(intent)
                }
            }
        }
    }

    companion object {
        const val VIEW_TYPE_LOADING = 1
        const val VIEW_TYPE_MOVIE = 2

        private val COMPARATOR = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Movie,
                newItem: Movie
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}