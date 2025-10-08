package com.andriawan.moviedb.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.andriawan.moviedb.databinding.ItemActorBinding
import com.andriawan.moviedb.domain.models.Cast
import com.andriawan.moviedb.utils.Constants.IMAGE_BASE_URL
import com.andriawan.moviedb.utils.extensions.loadImageRounded

class ActorAdapter : ListAdapter<Cast, ActorAdapter.ViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemActorBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemActorBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(cast: Cast) {
            binding.apply {
                ivActor.loadImageRounded(IMAGE_BASE_URL + cast.profilePath)

                tvName.text = cast.name
                tvCharacterName.text = cast.character
            }
        }
    }

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<Cast>() {
            override fun areItemsTheSame(oldItem: Cast, newItem: Cast): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Cast, newItem: Cast): Boolean {
                return oldItem == newItem
            }
        }
    }
}