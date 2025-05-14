package com.yun.seoul.moduta.ui.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.yun.seoul.moduta.R
import com.yun.seoul.moduta.base.BaseFullScreenDialog
import com.yun.seoul.moduta.base.BaseRecyclerViewAdapter
import com.yun.seoul.moduta.base.BindingAdapters.replace
import com.yun.seoul.moduta.databinding.DialogSearchBusBinding
import com.yun.seoul.moduta.util.ApiUtil.apiResultEmpty
import com.yun.seoul.moduta.util.ApiUtil.apiResultError
import com.yun.seoul.moduta.util.dialogResize
import com.yun.seoul.moduta.util.setOnSingleClickListener

interface BusSearchInterface<T> {
    fun keywordResult(keyword: String)
    fun onSelectedItem(item: T)
}

class BusSearchDialog<T : Any, B : ViewDataBinding>(
    context: Context,
    private val routeSearchInterface: BusSearchInterface<T>,
    private val layoutResId: Int,
    private val bindingVariableId: Int,
    private val bindingListenerId: Int,
) : BaseFullScreenDialog(context) {

    private lateinit var binding: DialogSearchBusBinding
    private var keyword: String = ""

    fun onEmpty(){
        apiResultEmpty(binding.root)
    }

    fun onError(error: String) {
        apiResultError(error, binding.root)
    }

    fun routeSearchDataUpdate(searchInfoList: List<T>) {
        if (this::binding.isInitialized) {
            binding.rvBusRouteSearch.replace(searchInfoList)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_search_bus,
            null,
            false
        )
        setContentView(binding.root)


        keyword = "146"
        binding.icInputEdit.etInput.setText(keyword)


        context.dialogResize(this, 1.0f, 1.0f)

        binding.icInputEdit.etInput.doOnTextChanged { text, _, _, _ ->
            keyword = text.toString()
        }

        binding.icInputEdit.btnSearch.setOnSingleClickListener {
            if (keyword.isNotEmpty()) {
                routeSearchInterface.keywordResult(keyword)
            } else {
                Toast.makeText(context, "검색어를 입력해 주세요", Toast.LENGTH_SHORT).show()
            }
        }

        binding.rvBusRouteSearch.run {
            adapter = object : BaseRecyclerViewAdapter.Create<T, B>(
                layoutResId = layoutResId,
                bindingVariableId = bindingVariableId,
                bindingListener = bindingListenerId
            ) {
                override fun onItemLongClick(item: T, view: View): Boolean = true
                override fun onItemClick(item: T, view: View) {
                    routeSearchInterface.onSelectedItem(item)
                }
            }
            setHasFixedSize(true)
            itemAnimator = null
        }
    }
}