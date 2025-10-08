package com.andriawan.moviedb.utils.extensions

import android.widget.ImageView
import com.andriawan.moviedb.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

fun ImageView.loadImageRounded(url: String) {
    Glide.with(this.context)
        .load(url)
        .centerCrop()
        .apply(RequestOptions.bitmapTransform(RoundedCorners(16.px)))
        .placeholder(R.drawable.img_placeholder)
        .error(R.drawable.img_placeholder)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}

fun ImageView.loadImage(url: String) {
    Glide.with(this.context)
        .load(url)
        .centerCrop()
        .placeholder(R.drawable.img_placeholder)
        .error(R.drawable.img_placeholder)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}