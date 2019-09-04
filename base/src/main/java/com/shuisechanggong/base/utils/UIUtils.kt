package com.shuisechanggong.base.utils

import android.widget.Toast
import com.shuisechanggong.base.BaseApplication

/**
 *
 */

fun toast(message: String?) {
    if (message != null)
        Toast.makeText(AppUtils.getApp(), message, Toast.LENGTH_SHORT)
}