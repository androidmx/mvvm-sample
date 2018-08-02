package com.gigigo.mvvm.core

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager

/**
 * @author JG - August 02, 2018
 * @version 0.0.1
 * @since 0.0.1
 */
class EndlessScrollListener(private var layoutManager: RecyclerView.LayoutManager,
                            private var scrollListener: EndlessScrollListener.ScrollListener) :
        RecyclerView.OnScrollListener() {

    private var visibleThreshold: Int = 5
    private var currentPage: Int = 0
    private var previousTotalItemCount: Int = 0
    private var previousVisiblePosition: Int = 0
    private var startingPageIndex: Int = 0
    private var scrolledDistance: Int = 0
    private var loading: Boolean = false
    private var controlsVisible: Boolean = false
    private var layoutManagerType: Int = LAYOUT_MANAGER_LINEAR

    companion object {
        private const val HIDE_THRESHOLD = 20
        private const val LAYOUT_MANAGER_LINEAR = 0
        private const val LAYOUT_MANAGER_GRID = 1
        private const val LAYOUT_MANAGER_STAGGERED_GRID = 2
    }

    init {
        when(this.layoutManager) {
            is StaggeredGridLayoutManager -> {
                this.layoutManagerType = LAYOUT_MANAGER_STAGGERED_GRID
                this.visibleThreshold = visibleThreshold *
                        (layoutManager as StaggeredGridLayoutManager).spanCount
            }
            is GridLayoutManager -> {
                this.layoutManagerType = LAYOUT_MANAGER_GRID
                this.visibleThreshold = visibleThreshold *
                        (layoutManager as GridLayoutManager).spanCount
            }
            is LinearLayoutManager -> {
                this.layoutManagerType = LAYOUT_MANAGER_LINEAR
            }
        }
    }

    private fun getLastVisibleItem(lastVisibleItemsPositions:IntArray): Int {
        var maxSize = 0

        for (index in lastVisibleItemsPositions.indices)
        {
            if (index == 0)
            {
                maxSize = lastVisibleItemsPositions[index]
            }
            else if (lastVisibleItemsPositions[index] > maxSize)
            {
                maxSize = lastVisibleItemsPositions[index]
            }
        }

        return maxSize
    }

    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        var lastVisibleItemPosition = 0
        val totalItemCount = layoutManager.itemCount

        when (layoutManagerType) {
            LAYOUT_MANAGER_STAGGERED_GRID -> {
                val lastVisibleItemPositions = (layoutManager as StaggeredGridLayoutManager)
                        .findLastVisibleItemPositions(null)
                lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions)
            }
            LAYOUT_MANAGER_GRID -> {
                lastVisibleItemPosition = (layoutManager as GridLayoutManager)
                        .findLastVisibleItemPosition()
            }
            LAYOUT_MANAGER_LINEAR -> {
                lastVisibleItemPosition = (layoutManager as LinearLayoutManager)
                        .findLastVisibleItemPosition()
            }
        }

        if(totalItemCount < previousTotalItemCount) {
            currentPage = startingPageIndex
            previousTotalItemCount = totalItemCount

            if(totalItemCount == 0) loading = true
        }

        if(loading && (totalItemCount > previousTotalItemCount)) {
            loading = false
            previousTotalItemCount = totalItemCount
        }

        if(!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
            if(previousVisiblePosition != lastVisibleItemPosition) {
                currentPage++
                previousVisiblePosition = lastVisibleItemPosition
                scrollListener.onLoadMore(currentPage, totalItemCount, view)
            }
            loading = true
        }

        setupVisibility(dy)
    }

    private fun setupVisibility(dy: Int) {
        if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
            scrollListener.onHide()
            controlsVisible = false
            scrolledDistance = 0
        } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
            scrollListener.onShow()
            controlsVisible = true
            scrolledDistance = 0
        }

        if ((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)) {
            scrolledDistance += dy
        }
    }

    // Call this method whenever performing new searches
    fun resetState() {
        this.currentPage = this.startingPageIndex
        this.previousTotalItemCount = 0
        this.previousVisiblePosition = 0
        this.loading = true
    }

    interface ScrollListener {
        fun onHide()
        fun onShow()
        fun onLoadMore(page:Int, totalItemsCount:Int, view:RecyclerView)
    }
}