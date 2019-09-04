package com.shuisechanggong.base

import androidx.lifecycle.ViewModelProvider

/**
 *
 */


interface BaseView{
    fun initViews()
    fun initViewModel(provider:ViewModelProvider)

}