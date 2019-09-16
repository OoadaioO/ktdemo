package com.shuisechanggong.ktdemo.view

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shuisechanggong.base.BaseActivity
import com.shuisechanggong.ktdemo.R
import kotlinx.android.synthetic.main.activity_main.*

/**
 *
 */

class MainActivity : BaseActivity() {

    override fun initViews() {
        setContentView(R.layout.activity_main)

        recycleView.layoutManager = LinearLayoutManager(this)

        val adapter = Adapter(getActivities())
        recycleView.adapter = adapter;
        adapter.notifyDataSetChanged()

    }

    fun getActivities() :List<Class<out Activity>>{

        val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)

        val list = ArrayList<Class<out Activity>>()
        for (activityInfo in packageInfo.activities) {
            try {
                if (activityInfo.name.startsWith(packageName) && !activityInfo.name.equals(MainActivity::class.java.name)) {
                    val element = Class.forName(activityInfo.name) as Class<out Activity>
                    list.add(element)
                }
            } catch (ignore: Exception) {

            }
        }
        return list

    }

    override fun initViewModel(provider: ViewModelProvider) {

    }


    private inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text: TextView

        init {
            text = itemView.findViewById(android.R.id.text1)
        }

    }

    private inner class Adapter(val list: List<Class<out Activity>>) : RecyclerView.Adapter<ViewHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false))
        }

        override fun getItemCount(): Int {
            return list?.size ?: 0
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            holder.text.text = list?.get(position)?.simpleName
            holder.itemView.setOnClickListener {
                var intent = Intent(this@MainActivity, list.get(position))
                startActivity(intent)
            }

        }

    }
}