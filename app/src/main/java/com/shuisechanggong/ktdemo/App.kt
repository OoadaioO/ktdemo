package com.shuisechanggong.ktdemo

import com.shuisechanggong.base.BaseApplication
import com.shuisechanggong.base.http.RetrofitClient

/**
 *
 */

class App : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        RetrofitClient.init(Config.baseUrl)
    }
}

