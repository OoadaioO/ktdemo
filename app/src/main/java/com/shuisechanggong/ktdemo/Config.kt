package com.shuisechanggong.ktdemo

import com.shuisechanggong.ktdemo.BuildConfig

/**
 *
 */

object Config {
    val baseUrl: String
        get() = when (BuildConfig.BUILD_TYPE) {
            "debug" -> "https://www.mxnzp.com/api/"
            else -> "https://www.mxnzp.com/api/"
        }

}