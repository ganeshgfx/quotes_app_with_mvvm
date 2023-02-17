package com.ganeshgfx.quotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ganeshgfx.quotes.api.QuoteService
import com.ganeshgfx.quotes.api.RetrofitHelper
import com.ganeshgfx.quotes.databinding.ActivityMainBinding
import com.ganeshgfx.quotes.repository.QuotesRepository
import com.ganeshgfx.quotes.utils.NetworkUtils
import com.ganeshgfx.quotes.viewmodels.MainViewModel
import com.ganeshgfx.quotes.viewmodels.MainViewModelFactory
import com.google.android.material.elevation.SurfaceColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.ganeshgfx.quotes.models.Result

class MainActivity : AppCompatActivity() {

    val TAG = "TAG"

    private lateinit var mainViewModel: MainViewModel

    private lateinit var binding: ActivityMainBinding

    private lateinit var myAdapter : MainRecycleViewAdapter


   // private val myAdapter by

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        //setContentView(binding.root)

        val color = SurfaceColors.SURFACE_2.getColor(this)
        window.statusBarColor = color
        window.navigationBarColor = color

        val repository = (application as QuoteApplication).quotesRepository

        mainViewModel =
            ViewModelProvider(this, MainViewModelFactory(repository)).get(MainViewModel::class.java)

        myAdapter = MainRecycleViewAdapter()

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        binding.quoteRecyclerview.layoutManager = linearLayoutManager
        binding.quoteRecyclerview.adapter =  myAdapter

        mainViewModel.quotes.observe(this) {
            var newList:MutableList<Result> = it.results.toMutableList()

            newList.removeAt(newList.size-1)

            myAdapter.setData(newList)
            binding.quoteRecyclerview.smoothScrollToPosition(myAdapter.itemCount-1)
        }

        mainViewModel.randomQuote.observe(this) {
            //Log.d(TAG, "onCreate: ${it.content}")
            //binding.tv.text = it.content
        }

        //Log.d("NET", "isOnline : ${NetworkUtils.isOnline(this)}")

        binding.refresh.setOnRefreshListener {
            GlobalScope.launch(Dispatchers.IO) {
                mainViewModel.refreshQuote()
                binding.refresh.isRefreshing = false
            }
        }


        binding.data = mainViewModel
        binding.lifecycleOwner = this
    }
}