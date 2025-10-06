package com.andriawan24.moviedb.ui.main

import com.andriawan24.moviedb.base.BaseActivity
import com.andriawan24.moviedb.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initUI() {
        // TODO: Implement UI components
    }
}