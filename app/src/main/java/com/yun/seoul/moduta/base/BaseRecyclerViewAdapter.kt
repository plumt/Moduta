package com.yun.seoul.moduta.base

import android.graphics.Canvas
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filterable
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import android.widget.Filter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewAdapter {
    interface OnItemClickListener<T, V> {
        fun onItemClick(item: T, view: V)
        fun onItemLongClick(item: T, view: V): Boolean
    }

    abstract class Create<T : Any, B : ViewDataBinding>(
        @LayoutRes private val layoutResId: Int,
        private val bindingVariableId: Int,
        private val bindingListener: Int
    ) : RecyclerView.Adapter<BaseViewHolder<B>>(), OnItemClickListener<T, View>, Filterable {

        var mItems = MutableLiveData<ArrayList<T>>(arrayListOf())

        override fun getFilter(): Filter? = null

        fun attachSwipeToDelete(recyclerView: RecyclerView, callback: (T) -> Unit) {
            val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
                override fun onItemSwiped(position: Int) {
                    callback(mItems.value!![position])
                    mItems.value!!.removeAt(position)
                    notifyItemRemoved(position)
                }
            }
            val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
            itemTouchHelper.attachToRecyclerView(recyclerView)
        }

        fun replaceAll(items: List<T>) {
            mItems.value!!.clear()
            mItems.value!!.addAll(items)
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return mItems.value?.size ?: 0
                }

                override fun getNewListSize(): Int {
                    return items.size
                }

                override fun areItemsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ): Boolean {
                    return mItems.value!![oldItemPosition] == items[newItemPosition]
                }

                override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ): Boolean {
                    val newProduct = items[newItemPosition]
                    val oldProduct = mItems.value!![oldItemPosition]
                    return newProduct == oldProduct
                }
            })
            result.dispatchUpdatesTo(this)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<B> {
            val resId: Int = layoutResId

            val binding: B = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                resId,
                parent,
                false
            )
            binding.setVariable(bindingListener, this)
            return object : BaseViewHolder<B>(binding) {}
        }

        override fun onBindViewHolder(holder: BaseViewHolder<B>, position: Int) {
            holder.binding.setVariable(bindingVariableId, mItems.value!![position])
            holder.binding.executePendingBindings()
        }

        override fun getItemCount() = mItems.value!!.size
    }

    abstract class SwipeToDeleteCallback : ItemTouchHelper.Callback() {
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
            return makeMovementFlags(0, swipeFlags)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            onItemSwiped(position)
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            val itemWidth = viewHolder.itemView.width.toFloat()
            val itemHeight = viewHolder.itemView.height.toFloat()
            val alpha = 1.0f - Math.abs(dX) / itemWidth
            viewHolder.itemView.alpha = alpha

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }

        override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
            return 0.1f // 스와이프 거리 비율을 0.1로 설정합니다.
        }

        abstract fun onItemSwiped(position: Int)
    }

    abstract class BaseViewHolder<B : ViewDataBinding>(val binding: B) : RecyclerView.ViewHolder(
        binding.root
    )
}

class RecyclerViewDecorationHorizontal(
    private val horizontalMargin: Int,
    private val verticalMargin: Int = 0
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        outRect.left = horizontalMargin
        outRect.right = horizontalMargin
        outRect.top = verticalMargin
        outRect.bottom = verticalMargin
    }
}