package com.example.kotlin.ui.component.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.kotlin.SPLASH_DELAY
import com.example.kotlin.databinding.SplashActivityBinding
import com.example.kotlin.ui.base.BaseActivity
import com.example.kotlin.ui.component.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashWithActivity : BaseActivity() {

    private lateinit var binding: SplashActivityBinding

    override fun observeViewModel() {
    }

    override fun initViewBinding() {
        binding = SplashActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigateToMainScreen()
    }

    private fun navigateToMainScreen() {
        Handler(Looper.getMainLooper()).postDelayed(
            {
                val nextScreenIntent = Intent(this, LoginActivity::class.java)
                startActivity(nextScreenIntent)
                finish()
            }, SPLASH_DELAY.toLong()
        )
    }


}