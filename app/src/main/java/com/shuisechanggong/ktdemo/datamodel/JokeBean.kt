package com.shuisechanggong.ktdemo.datamodel

import com.google.gson.annotations.SerializedName

data class JokeBean(@SerializedName("content") val content: String? = null, @SerializedName("updateTime") val updateTime: String? = null) {
    override fun toString(): String {
        return "ListBean(content='$content', updateTime='$updateTime')"
    }
}