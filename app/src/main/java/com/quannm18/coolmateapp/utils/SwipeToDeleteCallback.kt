package com.quannm18.coolmateapp.utils

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

abstract class SwipeToDeleteCallback : ItemTouchHelper.Callback() {
    private val limitScrollX = CommonUtils.dpToPx(100)
    private var currentScrollX = 0
    private var currentScrollWhenInActive = 0
    private var initXWhenInActive = 0f
    private var firstInActive = false
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val swipeFlag = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        return makeMovementFlags(0, swipeFlag)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return Integer.MAX_VALUE.toFloat()
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return Integer.MAX_VALUE.toFloat()
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
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (dX == 0f) {
                currentScrollX = viewHolder.itemView.scrollX
                firstInActive = false
            }
            if (isCurrentlyActive) {
                var scrollOffset = currentScrollX + (-dX).toInt()
                if (scrollOffset > limitScrollX) {
                    scrollOffset = limitScrollX
                } else if (scrollOffset < 0) {
                    scrollOffset = 0
                }
                viewHolder.itemView.scrollTo(scrollOffset, 0)
            } else {
                if (firstInActive) {
                    firstInActive = false
                    currentScrollWhenInActive = viewHolder.itemView
                        .scrollX
                    initXWhenInActive = dX
                }

                if (viewHolder.itemView.scrollX < limitScrollX) {
                    viewHolder.itemView.scrollTo(
                        (currentScrollWhenInActive * dX / initXWhenInActive).toInt(),
                        0
                    )
                }
            }
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        if (viewHolder.itemView.scrollX > limitScrollX) {
            viewHolder.itemView.scrollTo(limitScrollX, 0)
        } else if (viewHolder.itemView.scrollX < 0) {
            viewHolder.itemView.scrollTo(0, 0)
        }
    }
}