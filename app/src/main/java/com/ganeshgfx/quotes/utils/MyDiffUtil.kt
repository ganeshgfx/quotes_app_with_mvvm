package com.ganeshgfx.quotes.utils

import androidx.recyclerview.widget.DiffUtil
import com.ganeshgfx.quotes.models.Result

class MyDiffUtil(
    private val oldList: List<Result>,
    private val newList: List<Result>
) : DiffUtil.Callback() {
    override fun getOldListSize():Int = oldList.size

    override fun getNewListSize():Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]._id == newList[newItemPosition]._id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when{
            oldList[oldItemPosition]._id != newList[newItemPosition]._id -> { false }
            oldList[oldItemPosition].content != newList[newItemPosition].content -> { false }
            oldList[oldItemPosition].author != newList[newItemPosition].author -> { false }
            oldList[oldItemPosition].authorSlug != newList[newItemPosition].authorSlug -> { false }
            oldList[oldItemPosition].dateAdded != newList[newItemPosition].dateAdded -> { false }
            oldList[oldItemPosition].dateModified != newList[newItemPosition].dateModified -> { false }
            oldList[oldItemPosition].length != newList[newItemPosition].length -> { false }
            else -> true
        }
    }
}