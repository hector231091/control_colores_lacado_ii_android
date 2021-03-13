package com.hectormorales.colores_cabina_lacado_ii_android.view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class SpaceItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val halfSpace = space shr 1
        outRect.set(halfSpace, 0, halfSpace, space)

        val childIndex = parent.getChildLayoutPosition(view)
        val spanCount = getSpanCount(parent)
        val spanIndex = childIndex % spanCount

        if (spanIndex <= 0) {
            // Left edge
            outRect.left = space
        }
        if (spanIndex + 1 >= spanCount) {
            // Right edge
            outRect.right = space
        }
        if (childIndex < spanCount) {
            // Top edge
            outRect.top = space
        }
    }

    private fun getSpanCount(parent: RecyclerView): Int {
        val manager = parent.layoutManager
        if (manager is GridLayoutManager) {
            return manager.spanCount
        } else if (manager is StaggeredGridLayoutManager) {
            return manager.spanCount
        } else if (manager is LinearLayoutManager) {
            if (manager.orientation == LinearLayoutManager.HORIZONTAL) {
                return parent.adapter?.itemCount ?: 1
            }
        }
        return 1
    }
}
