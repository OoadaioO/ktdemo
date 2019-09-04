package com.shuisechanggong.base

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import kotlin.reflect.KClass

/**
 *
 */

open abstract class BaseActivity : FragmentActivity(), BaseView {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel(ViewModelProviders.of(this))
        initViews()
    }

    fun <T : BaseViewModel> get(cls: Class<T>): T {
        return ViewModelProviders.of(this).get(cls)
    }

    fun <T : BaseViewModel> get(cls: KClass<T>): T {
        return ViewModelProviders.of(this).get(cls.java)
    }
}

