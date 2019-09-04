package com.shuisechanggong.base

import android.app.Application
import com.shuisechanggong.base.utils.AppUtils

/**
 *
 */

abstract class BaseApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        AppUtils.init(this)
    }

}