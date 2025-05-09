package com.yun.seoul.moduta.base

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

object BindingAdapters {

    @BindingAdapter("app:replaceAll")
    @JvmStatic
    fun RecyclerView.replace(list: List<Any>?) {
        list?.let {
            (adapter as? BaseRecyclerViewAdapter.Create<Any, *>)?.run {
                replaceAll(it)
            }
        }
    }
}