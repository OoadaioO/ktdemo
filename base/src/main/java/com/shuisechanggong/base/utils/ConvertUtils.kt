package com.shuisechanggong.base.utils

import com.shuisechanggong.base.BaseApplication

/**
 *
 */

fun dp2px(dpValue: Float): Int {
    val scale = AppUtils.getApp().getResources().getDisplayMetrics().density
    return (dpValue * scale + 0.5f).toInt()
}