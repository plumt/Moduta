package com.yun.seoul.moduta.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.ViewDataBinding
import com.yun.seoul.moduta.R
import com.yun.seoul.moduta.BR
import com.yun.seoul.moduta.databinding.ViewSearchBarBinding
import com.yun.seoul.moduta.ui.dialog.BusSearchDialog
import com.yun.seoul.moduta.ui.dialog.BusSearchInterface
import com.yun.seoul.moduta.util.setOnSingleClickListener

class BusSearchBarView<T : Any> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var binding: ViewSearchBarBinding
    private lateinit var routeSearchDialog: BusSearchDialog<T, ViewDataBinding>
    private var searchCallback: ((String) -> Unit)? = null
    private var selectedItemCallback: ((T) -> Unit)? = null
    private var searchType: Int = SEARCH_TYPE_BUS

    companion object {
        const val SEARCH_TYPE_BUS = 0 // 버스 검색
        const val SEARCH_TYPE_STATION = 1 // 정거장 검색
    }

    init {
        initView(attrs)
    }

    @Suppress("UNCHECKED_CAST")
    private fun initView(attrs: AttributeSet?){
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.TransportSearchBarView
        ).run {
            searchType = getInt(R.styleable.TransportSearchButtonView_searchType,
                SEARCH_TYPE_BUS
            )
            this
        }

        typedArray.recycle()
        binding = ViewSearchBarBinding.inflate(LayoutInflater.from(context), this, true)

        val (layoutResId, bindingVariableId, bindingListenerId) = when (searchType) {
            SEARCH_TYPE_BUS -> Triple(
                R.layout.item_bus_search_bus_info_list,
                BR.itemBusSearchBusInfo,
                BR.busSearchBusInfoListener
            )

            SEARCH_TYPE_STATION -> Triple(
                R.layout.item_bus_search_bus_info_list,
                BR.itemBusSearchBusInfo,
                BR.busSearchBusInfoListener
            )

            else -> return
        }

        routeSearchDialog = BusSearchDialog(
            context,
            object : BusSearchInterface<T> {
                override fun keywordResult(keyword: String) {
                    searchCallback?.invoke(keyword)
                }

                override fun onSelectedItem(item: T) {
                    selectedItemCallback?.invoke(item)
                    routeSearchDialog.dismiss()
                }
            },
            layoutResId, bindingVariableId, bindingListenerId
        )

        binding.llSearch.setOnSingleClickListener {
            routeSearchDialog.show()
        }
    }

    fun setOnSearchListener(callback: (String) -> Unit) {
        this.searchCallback = callback
    }

    fun setOnSelectedListener(callback: (T) -> Unit) {
        this.selectedItemCallback = callback
    }

    fun updateData(searchInfoList: List<T>) {
        routeSearchDialog.routeSearchDataUpdate(searchInfoList)
    }

    fun emptyData(){
        routeSearchDialog.onEmpty()
    }

    fun errorData(error: String){
        routeSearchDialog.onError(error)
    }
}