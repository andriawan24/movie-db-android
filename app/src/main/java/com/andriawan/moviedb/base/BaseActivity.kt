package com.andriawan.moviedb.base

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewbinding.ViewBinding
import com.andriawan.moviedb.R

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    abstract val binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        initIntent()
        initUI()
        initListener()
    }

    open fun initIntent() = Unit
    abstract fun initUI()
    open fun initListener() = Unit

    protected fun setupSystemPadding(isVerticalEnabled: Boolean = false) {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                if (isVerticalEnabled) systemBars.top else 0,
                systemBars.right,
                if (isVerticalEnabled) systemBars.bottom else 0
            )

            insets
        }
    }
}