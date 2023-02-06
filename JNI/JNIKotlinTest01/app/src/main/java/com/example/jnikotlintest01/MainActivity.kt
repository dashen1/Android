package com.example.jnikotlintest01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.jnikotlintest01.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    /**
     * A native method that is implemented by the 'jnikotlintest01' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    external fun sort(arr: IntArray)

    companion object {
        // Used to load the 'jnikotlintest01' library on application startup.类似静态代码块
        init {
            System.loadLibrary("jnikotlintest01")
        }
    }
}