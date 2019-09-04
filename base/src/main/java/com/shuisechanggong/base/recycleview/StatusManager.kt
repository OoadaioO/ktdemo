package com.shuisechanggong.base.recycleview

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 *
 */
class StatusManager private constructor(private val recyclerView: RecyclerView, private val layoutManager: LinearLayoutManager, private val adapter: StatusAdapter<*>?) {
    var pageIndex = 1
        private set
    private var prefetchCount = 5
    private var isPageEnd = false
    private var isPageLoading = false
    private var isContentEmpty = true
    private var pageCallback: PageCallback? = null

    init {
        this.recyclerView.layoutManager = layoutManager
        this.recyclerView.adapter = adapter
    }

    fun setPrefetchCount(prefetchCount: Int) {
        this.prefetchCount = prefetchCount
    }


    fun setPageCallback(pageCallback: PageCallback) {
        this.pageCallback = pageCallback
        setUpPageLoadScrollListener()
    }

    private fun setUpPageLoadScrollListener() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    return
                }
                if (isPageLoading) {
                    return
                }
                if (isPageEnd) {
                    return
                }
                if (isContentEmpty) {
                    return
                }
                val count = layoutManager.itemCount - layoutManager.findLastVisibleItemPosition()
                if (count > prefetchCount) {
                    return
                }
                isPageLoading = true
                pageCallback?.onPageLoad(pageIndex)
            }
        })
    }

    fun resetPageIndex() {
        pageIndex = 1
    }

    fun setContentEmptyNotice(text: String) {
        if (adapter != null) {
            adapter.setContentEmptyNotice(text)
        }
    }

    fun setFooterLoadingNotice(text: String) {
        if (adapter != null) {
            adapter.setFooterLoadingNotice(text)
        }
    }

    fun setFooterFailNotice(text: String) {
        if (adapter != null) {
            adapter.setFooterFailNotice(text)
        }
    }

    fun setFooterEndNotice(text: String) {
        if (adapter != null) {
            adapter.setFooterEndNotice(text)
        }
    }

    fun setOnEmptyContentClickListener(listener: StatusAdapter.OnEmptyContentClickListener) {
        if (adapter != null) {
            adapter.setOnEmptyContentClickListener(listener)
        }
    }

    fun setOnFailFooterClickListener(listener: StatusAdapter.OnFailFooterClickListener) {
        if (adapter != null) {
            adapter.setOnFailFooterClickListener(listener)
        }
    }


    fun notifySuccessStatus(isPageEnd: Boolean) {
        isPageLoading = false
        this.isPageEnd = isPageEnd
        isContentEmpty = adapter?.count == 0
        if (isContentEmpty) {
            notifyContentEmpty()
            return
        }
        if (isPageEnd) {
            notifyFooterEnd()
        } else {
            notifyFooterLoading()
        }
    }

    fun notifyFailStatus() {
        isPageLoading = false
        isContentEmpty = adapter?.count == 0
        if (isContentEmpty) {
            notifyContentEmpty()
            return
        }
        notifyFooterFail()
    }


    private fun notifyContentEmpty() {
        refreshStatus(StatusAdapter.TYPE_CONTENT_EMPTY)
    }

    private fun notifyFooterLoading() {
        refreshStatus(StatusAdapter.TYPE_FOOTER_LOADING)
    }

    private fun notifyFooterFail() {
        refreshStatus(StatusAdapter.TYPE_FOOTER_FAIL)
    }

    private fun notifyFooterEnd() {
        refreshStatus(StatusAdapter.TYPE_FOOTER_END)
    }


    private fun refreshStatus(@StatusAdapter.Status status: Int) {
        adapter?.notifyStatusChange(status)
    }

    interface PageCallback {
        fun onPageLoad(pageIndex: Int)
    }

    companion object {

        fun initRecyclerView(recyclerView: RecyclerView, layoutManager: LinearLayoutManager, adapter: StatusAdapter<*>): StatusManager {
            return StatusManager(recyclerView, layoutManager, adapter)
        }
    }
}