package com.example.kotlin.ui.component.main

import android.os.Bundle
import com.example.kotlin.databinding.ActivityHomeBinding
import com.example.kotlin.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainCurrentActivity: BaseActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun observeViewModel() {
        TODO("Not yet implemented")
    }

    override fun initViewBinding() {
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}