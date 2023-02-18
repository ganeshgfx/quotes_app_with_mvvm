package com.ganeshgfx.quotes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ganeshgfx.quotes.databinding.QuoteItemBinding
import com.ganeshgfx.quotes.models.Result
import com.ganeshgfx.quotes.utils.MyDiffUtil

class MainRecycleViewAdapter : RecyclerView.Adapter<MainRecycleViewAdapter.MyViewHolder>() {

    private var quoteList = emptyList<Result>()

    class MyViewHolder(val binding : QuoteItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(QuoteItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.textView.text = "${quoteList[position].content} \n\t ~ ${quoteList[position].author}"
    }

    override fun getItemCount(): Int {
        return quoteList.size
    }

    fun setData(newList: List<Result>){
        val diffUtil = MyDiffUtil(quoteList,newList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        quoteList = newList
        diffResult.dispatchUpdatesTo(this)
    }
}