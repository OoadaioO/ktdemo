package com.shuisechanggong.base.http

import com.google.gson.JsonElement
import com.google.gson.JsonNull

/**
 *
 */

data class ServerResult<T>(val code: Int, val msg: String? = null,internal val data: JsonElement? = JsonNull.INSTANCE){

}

