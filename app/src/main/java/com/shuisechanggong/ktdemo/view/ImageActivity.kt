package com.shuisechanggong.ktdemo.view

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shuisechanggong.base.BaseActivity
import com.shuisechanggong.base.http.Response
import com.shuisechanggong.base.recycleview.StatusAdapter
import com.shuisechanggong.base.recycleview.StatusManager
import com.shuisechanggong.base.utils.dp2px
import com.shuisechanggong.base.utils.toast
import com.shuisechanggong.ktdemo.R
import com.shuisechanggong.ktdemo.datamodel.ImageBean
import kotlinx.android.synthetic.main.activity_image.*
import kotlinx.android.synthetic.main.activity_image_list_item.view.*

/**
 *
 */
class ImageActivity : BaseActivity() {
    val adapter: Adapter =
            Adapter()
    val viewModel: ImageViewModel by lazy {
        get(ImageViewModel::class.java)
    }

    var statusManager: StatusManager? = null

    override fun initViewModel(provider: ViewModelProvider) {


        viewModel.requestLiveData.observe(this, object : Observer<Response<List<ImageBean>>> {
            override fun onChanged(response: Response<List<ImageBean>>) {
                refreshLayout.isRefreshing = false
                if (response.isSuccess) {
                    val isPageEnd = response.data?.isEmpty() ?: true
                    if (!isPageEnd) {
                        adapter.data.clear()
                        adapter.data.addAll(response.data ?: ArrayList())
                        adapter.notifyDataSetChanged()
                    }
                    statusManager?.notifySuccessStatus(false)
                } else {
                    toast(response.error?.message)
                    statusManager?.notifySuccessStatus(false)
                }

            }
        })

        viewModel.loadLiveData.observe(this, object : Observer<Response<List<ImageBean>>> {
            override fun onChanged(response: Response<List<ImageBean>>) {
                refreshLayout.isEnabled = true
                if (response.isSuccess) {
                    val isPageEnd = response.data?.isEmpty() ?: true
                    if (!isPageEnd) {
                        adapter.data.addAll(response.data ?: ArrayList())
                        adapter.notifyDataSetChanged()
                    }
                    statusManager?.notifySuccessStatus(false)
                } else {
                    toast(response.error?.message)
                    statusManager?.notifySuccessStatus(false)
                }
            }
        })

    }

    override fun initViews() {
        setContentView(R.layout.activity_image)
        toolbar.setNavigationOnClickListener { finish() }

        val layoutManager = LinearLayoutManager(this)
        recycleView.layoutManager = layoutManager
        recycleView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            val margin = dp2px(15f)
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                val position = parent.getChildAdapterPosition(view)
                if (position == 0) {
                    outRect.top = margin
                }
                outRect.bottom = margin
            }

        })
        recycleView.adapter = adapter
        refreshLayout.setOnRefreshListener {
            viewModel.request()
        }
        statusManager = StatusManager.initRecyclerView(recycleView, layoutManager, adapter)
        statusManager?.setOnEmptyContentClickListener(object : StatusAdapter.OnEmptyContentClickListener {
            override fun onClick() {
                refreshLayout.isEnabled = false
                viewModel.request()
            }
        })
        statusManager?.setPageCallback(object : StatusManager.PageCallback {
            override fun onPageLoad(pageIndex: Int) {
                refreshLayout.isEnabled = false
                viewModel.load()
            }
        })


    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class Adapter(var data: ArrayList<ImageBean> = ArrayList()) : StatusAdapter<ViewHolder>() {
        override fun onCreateItem(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                            R.layout.activity_image_list_item,
                            parent,
                            false
                    )
            )
        }

        override fun onBindItem(holder: ViewHolder, position: Int) {
            var bean = data[position]


            try {
                var set = ConstraintSet()
                set.clone(holder.itemView.constraintLayout)
                set.setDimensionRatio(holder.itemView.image.id, bean.imageSize.replace("x", ":"))
                set.applyTo(holder.itemView.constraintLayout)

            } catch (ignore: Exception) {

            }
            Glide.with(holder.itemView.image)
                    .load(bean.imageUrl)
                    .into(holder.itemView.image)

        }

        override val count: Int
            get() = data.size


    }
}