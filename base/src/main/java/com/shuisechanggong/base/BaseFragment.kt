package com.shuisechanggong.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlin.reflect.KClass

/**
 *
 */

abstract class BaseFragment :Fragment(), BaseView {

    var rootView: View? = null


    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(createLayout(), container, false)
        return rootView
    }

    abstract fun createLayout(): Int


     fun <T : BaseViewModel> get(cls: Class<T>): T {
        return ViewModelProviders.of(this).get(cls)
    }

     fun <T : BaseViewModel> get(cls: KClass<T>): T {
        return ViewModelProviders.of(this).get(cls.java)
    }
}