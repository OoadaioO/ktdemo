package com.shuisechanggong.ktdemo.datamodel

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 *

 */
@Keep
data class JokeResponse(
    @SerializedName("page")
    val page: Int = 0,
    @SerializedName("totalCount")
    val totalCount: Int = 0,
    @SerializedName("limit")
    val limit: Int = 0,
    @SerializedName("list")
    val list: List<JokeBean> = ArrayList()
) {

    override fun toString(): String {
        return "JokeResponse(page=$page, totalCount=$totalCount, limit=$limit, list=$list)"
    }


}