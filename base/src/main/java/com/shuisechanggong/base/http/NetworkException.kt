package com.shuisechanggong.base.http

import com.google.gson.JsonParseException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.text.ParseException

/**
 *
 */

class NetworkException(override val message: String) : Exception(message) {

    companion object {
        fun from(e: Throwable): NetworkException = when (e) {
            is JsonParseException, is JSONException, is ParseException -> NetworkException(
                "解析异常"
            )
            is ConnectException, is SocketTimeoutException, is HttpException -> NetworkException(
                "网络异常"
            )
            else -> NetworkException(e.message ?: "未知错误")
        }

    }
}

