package com.andriawan.moviedb.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.andriawan.moviedb.databinding.ItemLoadingBinding

class MovieLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<MovieLoadStateAdapter.LoadingViewHolder>() {

    override fun onBindViewHolder(
        holder: LoadingViewHolder,
        loadState: LoadState
    ) = holder.bind(loadState)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingViewHolder {
        val binding = ItemLoadingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LoadingViewHolder(binding, retry)
    }

    inner class LoadingViewHolder(
        private val binding: ItemLoadingBinding,
        private val retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {
            binding.apply {
                if (loadState is LoadState.Error) {
                    tvErrMsg.text = loadState.error.localizedMessage
                }

                layoutError.isVisible = loadState is LoadState.Error
                pbLoading.isVisible = loadState is LoadState.Loading

                btnRetry.setOnClickListener {
                    retry()
                }
            }
        }
    }
}