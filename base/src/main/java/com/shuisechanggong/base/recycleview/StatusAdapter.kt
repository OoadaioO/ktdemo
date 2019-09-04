package com.shuisechanggong.base.recycleview

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.IntDef
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.shuisechanggong.base.R
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 *
 */

abstract class StatusAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    @Status
    private var status = TYPE_CONTENT_LOADING

    private var contentEmptyNotice = "数据为空"
    private var footerLoadingNotice = "加载中..."
    private var footerFailNotice = "加载失败,请点击重试"
    private var footerEndNotice = "已经到底了"

    private var onEmptyContentClickListener: OnEmptyContentClickListener? = null
    private var onFailFooterClickListener: OnFailFooterClickListener? = null

    open val count: Int = 0

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(
        TYPE_CONTENT_LOADING,
        TYPE_CONTENT_EMPTY,
        TYPE_FOOTER_LOADING,
        TYPE_FOOTER_END,
        TYPE_FOOTER_FAIL
    )
    annotation class Status


    final override fun getItemViewType(position: Int): Int {
        if (count == 0 || position == itemCount - 1) {
            return status
        } else {
            val itemType = getItemType(position)
            if (itemType <= TYPE_CONTENT_LOADING && itemType >= TYPE_FOOTER_FAIL) {
                throw IllegalArgumentException("getItemType不能为-1到-5区间内的值")
            }
            return itemType
        }
    }

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == status) {
            if (status == TYPE_CONTENT_LOADING) {
                ContentLoadingViewHolder(inflate(parent, R.layout.base_widgets_item_content_loading))
            } else if (status == TYPE_CONTENT_EMPTY) {
                ContentEmptyViewHolder(inflate(parent, R.layout.base_widgets_item_content_empty))
            } else if (status == TYPE_FOOTER_LOADING) {
                FooterLoadingViewHolder(inflate(parent, R.layout.base_widgets_item_footer_loading))
            } else if (status == TYPE_FOOTER_FAIL) {
                FooterFailViewHolder(inflate(parent, R.layout.base_widgets_item_footer_fail))
            } else {
                FooterEndViewHolder(inflate(parent, R.layout.base_widgets_item_footer_end))
            }
        } else {
            onCreateItem(parent, viewType)
        }
    }


    final override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        if (viewType < 0) {
            refreshStatus(holder)
        } else {
            onBindItem(holder as VH, position)
        }
    }

    final override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: List<Any>) {
        val viewType = getItemViewType(position)
        if (viewType < 0) {
            refreshStatus(holder)
        } else {
            onBindItem(holder as VH, position, payloads)
        }
    }


    override fun getItemCount(): Int {
        return count + 1
    }

    abstract fun onCreateItem(parent: ViewGroup, viewType: Int): VH

    abstract fun onBindItem(holder: VH, position: Int)

    fun onBindItem(holder: VH, position: Int, payloads: List<Any>) {
        this.onBindItem(holder, position)
    }

    fun getItemType(position: Int): Int {
        return 0
    }


    private fun refreshStatus(holder: RecyclerView.ViewHolder) {
        if (status == TYPE_CONTENT_LOADING) {
            (holder as StatusAdapter<*>.ContentLoadingViewHolder).refresh()
        } else if (status == TYPE_CONTENT_EMPTY) {
            (holder as StatusAdapter<*>.ContentEmptyViewHolder).refresh()
        } else if (status == TYPE_FOOTER_LOADING) {
            (holder as StatusAdapter<*>.FooterLoadingViewHolder).refresh()
        } else if (status == TYPE_FOOTER_FAIL) {
            (holder as StatusAdapter<*>.FooterFailViewHolder).refresh()
        } else {
            (holder as StatusAdapter<*>.FooterEndViewHolder).refresh()
        }
    }


    private fun inflate(parent: ViewGroup, @LayoutRes resource: Int): View {
        return LayoutInflater.from(parent.context).inflate(resource, parent, false)
    }


    internal fun setContentEmptyNotice(contentEmptyNotice: String) {
        this.contentEmptyNotice = contentEmptyNotice
    }

    internal fun setFooterLoadingNotice(footerLoadingNotice: String) {
        this.footerLoadingNotice = footerLoadingNotice
    }

    internal fun setFooterFailNotice(footerFailNotice: String) {
        this.footerFailNotice = footerFailNotice
    }

    internal fun setFooterEndNotice(footerEndNotice: String) {
        this.footerEndNotice = footerEndNotice
    }

    internal fun notifyStatusChange(@Status status: Int) {
        if (this.status == status) {
            return
        }
        this.status = status
        val handler = Handler(Looper.getMainLooper())
        handler.post { notifyItemChanged(itemCount - 1) }
    }

    internal fun setOnEmptyContentClickListener(onEmptyContentClickListener: OnEmptyContentClickListener) {
        this.onEmptyContentClickListener = onEmptyContentClickListener
    }

    internal fun setOnFailFooterClickListener(onFailFooterClickListener: OnFailFooterClickListener) {
        this.onFailFooterClickListener = onFailFooterClickListener
    }

    private inner class ContentEmptyViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val btnEmpty: ImageButton
        private val tvNotice: TextView

        init {
            btnEmpty = itemView.findViewById(R.id.btnEmpty)
            tvNotice = itemView.findViewById(R.id.tvNotice)
        }

        internal fun refresh() {
            tvNotice.text = contentEmptyNotice
            btnEmpty.setOnClickListener { v ->
                if (onEmptyContentClickListener != null) {
                    notifyStatusChange(TYPE_CONTENT_LOADING)
                    onEmptyContentClickListener!!.onClick()
                }
            }
        }
    }

    private inner class ContentLoadingViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal fun refresh() {

        }
    }


    private inner class FooterEndViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvFooter: TextView

        init {
            tvFooter = itemView.findViewById(R.id.tvFooter)
        }

        internal fun refresh() {
            tvFooter.text = footerEndNotice
        }
    }

    private inner class FooterLoadingViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvFooter: TextView

        init {
            tvFooter = itemView.findViewById(R.id.tvFooter)
        }

        internal fun refresh() {
            tvFooter.text = footerLoadingNotice
        }
    }

    private inner class FooterFailViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val btnRetry: Button

        init {
            btnRetry = itemView.findViewById(R.id.btnRetry)
        }

        internal fun refresh() {
            btnRetry.text = footerFailNotice
            btnRetry.setOnClickListener { v ->
                if (onFailFooterClickListener != null) {
                    notifyStatusChange(TYPE_FOOTER_LOADING)
                    onFailFooterClickListener!!.onClick()
                }
            }
        }
    }

    interface OnEmptyContentClickListener {
        fun onClick()
    }

    interface OnFailFooterClickListener {
        fun onClick()
    }


    companion object {

        internal const val TYPE_CONTENT_LOADING = -1
        internal const val TYPE_CONTENT_EMPTY = -2
        internal const val TYPE_FOOTER_LOADING = -3
        internal const val TYPE_FOOTER_END = -4
        internal const val TYPE_FOOTER_FAIL = -5
    }

}