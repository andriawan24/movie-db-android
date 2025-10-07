package com.andriawan.moviedb.utils.decorator

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andriawan.moviedb.utils.extensions.px

class GridSpacerDecorator(
    private val gridCount: Int,
    private val verticalMargin: Int = 16.px,
    private val horizontalMargin: Int = 16.px,
    private val outerVerticalMargin: Int = 6.px,
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        if (parent.layoutManager !is GridLayoutManager) {
            return
        }

        val position = parent.getChildAdapterPosition(view)
        val totalCount = state.itemCount

        // Add top outer margin
        if (position in (0..<gridCount)) {
            outRect.top = outerVerticalMargin
        }

        // Add horizontal margin
        val index = position % gridCount
        outRect.left = horizontalMargin * (gridCount - index) / gridCount
        outRect.right = horizontalMargin * (index + 1) / gridCount

        // Add bottom margin
        if (position >= totalCount - gridCount) {
            outRect.bottom = outerVerticalMargin
        } else {
            outRect.bottom = verticalMargin
        }
    }
}