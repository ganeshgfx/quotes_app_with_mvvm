package com.ganeshgfx.quotes

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.view.children
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
import com.ganeshgfx.quotes.viewmodels.QuotesViewModel
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {

    val TAG = "TAG"

    private lateinit var mainViewModel2: MainViewModel
    private lateinit var mainViewModel: QuotesViewModel

    private lateinit var binding: ActivityMainBinding

    private lateinit var myAdapter: MainRecycleViewAdapter
    

    // private val myAdapter by

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        //setContentView(binding.root)

        val color = SurfaceColors.SURFACE_2.getColor(this)
        window.statusBarColor = color
        window.navigationBarColor = color

        //ViewModel
//        val repository = (application as QuoteApplication).quotesRepository
//        mainViewModel2 =
//            ViewModelProvider(this, MainViewModelFactory(repository))[MainViewModel::class.java]

        //AndroidViewModel
        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        )[QuotesViewModel::class.java]

        //Recyclerview
        myAdapter = MainRecycleViewAdapter()
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        binding.quoteRecyclerview.layoutManager = linearLayoutManager
        binding.quoteRecyclerview.adapter = myAdapter
        mainViewModel.quotes.observe(this) {
            var newList: MutableList<Result> = it.results.toMutableList()
            if (newList.size > 0) {
                newList.removeAt(newList.size - 1)
            }
            myAdapter.setData(newList)
            if (myAdapter.itemCount >= 2)
                binding.quoteRecyclerview.smoothScrollToPosition(myAdapter.itemCount - 1)
        }

        binding.shareQuote.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.setType("text/plain")
            intent.putExtra(Intent.EXTRA_TEXT, mainViewModel.getTextToShare())
            startActivity(intent)

        }

        supportActionBar?.title = "Random Quotes"
        supportActionBar?.elevation = 10F

        binding.data = mainViewModel
        binding.lifecycleOwner = this
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh_quote -> {
                GlobalScope.launch(Dispatchers.Main) {
                    binding.refresh.isRefreshing = true
                    mainViewModel.refreshQuote()
                    binding.refresh.isRefreshing = false
                }
            }
            R.id.clear_history -> {
                if (mainViewModel.quotes.value?.results?.size!! > 1) {
                    MaterialAlertDialogBuilder(this)
                        .setTitle("Clear Quote History..?")
                        .setMessage("Tap 'Yes' to clear quote history and 'Cancel' to dismiss.")
                        .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
                        .setPositiveButton("Yes") { dialog, which ->
                            GlobalScope.launch(Dispatchers.Main){
                                mainViewModel.clearQuotes()
                            }
                        }
                        .show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

}