package com.andriawan.moviedb.utils.decorator

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andriawan.moviedb.utils.extensions.px

/**
 * A decorator for recyclerview that provides grid spacing for items in a grid layout manager
 *
 * The spacing is calculated to ensure equal spacing between all items while maintaining
 * consistent outer margins.
 *
 * @param gridCount The number of columns in the grid layout
 * @param verticalMargin The vertical spacing between rows in pixels (default: 24px)
 * @param horizontalMargin The horizontal spacing between columns in pixels (default: 16px)
 * @param outerVerticalMargin The vertical margin for the first and last rows in pixels (default: 24px)
 *
 * @author Naufal Fawwaz Andriawan
 */
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

        // This decorator only works with grid, otherwise reject
        if (parent.layoutManager !is GridLayoutManager) {
            return
        }

        val position = parent.getChildAdapterPosition(view)
        val totalCount = state.itemCount

        // Add top outer margin for items in the first row
        if (position in (0..<gridCount)) {
            outRect.top = outerVerticalMargin
        }

        // Calculate and apply horizontal margins
        // This ensures equal spacing between columns while maintaining outer margins
        val index = position % gridCount
        outRect.left = horizontalMargin * (gridCount - index) / gridCount
        outRect.right = horizontalMargin * (index + 1) / gridCount

        // Add bottom margin - outer margin for last row, regular margin for other rows
        if (position >= totalCount - gridCount) {
            outRect.bottom = outerVerticalMargin
        } else {
            outRect.bottom = verticalMargin
        }
    }
}