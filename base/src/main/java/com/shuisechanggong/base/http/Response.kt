package com.shuisechanggong.base.http

/**
 *
 */

data class Response<T>private constructor(val isSuccess: Boolean, val data: T?=null, val error: Throwable?=null){

    companion object{
        fun <T> success(data: T): Response<T> =
            Response(true, data)
        fun <T> fail(throwable: Throwable): Response<T> =
            Response(false, null, throwable)
    }
}
