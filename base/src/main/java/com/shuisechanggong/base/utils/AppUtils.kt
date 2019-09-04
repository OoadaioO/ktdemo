package com.shuisechanggong.base.utils

import android.app.Application

/**
 *
 */


class AppUtils {

    companion object {
        private var instance: Application? = null
        fun getApp(): Application = instance ?: throw NullPointerException("请初始化")
        fun init(application: Application){
            instance = application
        }
    }
}