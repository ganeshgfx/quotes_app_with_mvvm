package com.ganeshgfx.quotes

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ganeshgfx.quotes.databinding.QuoteItemBinding
import com.ganeshgfx.quotes.models.Result
import com.ganeshgfx.quotes.utils.MyDiffUtil

class MainRecycleViewAdapter : RecyclerView.Adapter<MainRecycleViewAdapter.MyViewHolder>() {

    private var quoteList = emptyList<Result>()

    class MyViewHolder(val binding: QuoteItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            QuoteItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = quoteList[position]
        holder.binding.data = item
        holder.binding.quoteItem.setOnLongClickListener {
            val textToCopy = "${item.content} ~ ${item.author}"
            val clipboardManager = it.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", textToCopy)
            clipboardManager.setPrimaryClip(clipData)
            true
        }
    }

    override fun getItemCount(): Int {
        return quoteList.size
    }

    fun setData(newList: List<Result>) {
        val diffUtil = MyDiffUtil(quoteList, newList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        quoteList = newList
        diffResult.dispatchUpdatesTo(this)
    }

    fun clearList(){
        setData(emptyList<Result>())
    }
}