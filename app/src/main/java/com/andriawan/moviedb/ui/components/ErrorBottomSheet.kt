package com.andriawan.moviedb.ui.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.andriawan.moviedb.databinding.LayoutErrorBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ErrorBottomSheet(private val message: String) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = LayoutErrorBottomSheetBinding.inflate(
            LayoutInflater.from(requireContext()),
            container,
            false
        )

        binding.tvErrMsg.text = message

        return binding.root
    }

    companion object {
        const val TAG = "ErrorBottomSheet"
    }
}