package com.ganeshgfx.quotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ganeshgfx.quotes.api.QuoteService
import com.ganeshgfx.quotes.api.RetrofitHelper
import com.ganeshgfx.quotes.databinding.ActivityMainBinding
import com.ganeshgfx.quotes.repository.QuotesRepository
import com.ganeshgfx.quotes.utils.NetworkUtils
import com.ganeshgfx.quotes.viewmodels.MainViewModel
import com.ganeshgfx.quotes.viewmodels.MainViewModelFactory
import com.google.android.material.elevation.SurfaceColors

class MainActivity : AppCompatActivity() {

    val TAG = "myLog"

    private lateinit var mainViewModel: MainViewModel

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val color = SurfaceColors.SURFACE_2.getColor(this)
        window.statusBarColor = color
        window.navigationBarColor = color

        val repository = (application as QuoteApplication).quotesRepository

        mainViewModel = ViewModelProvider(this, MainViewModelFactory(repository)).get(MainViewModel::class.java)

        mainViewModel.quotes.observe(this) {
            val q = it.results.map { it.toString() + "\n" }
            Log.d(TAG, "${q}")
            binding.tv.text = q.toString()
        }

        //Log.d("NET", "isOnline : ${NetworkUtils.isOnline(this)}")

        binding.data = mainViewModel
    }
}