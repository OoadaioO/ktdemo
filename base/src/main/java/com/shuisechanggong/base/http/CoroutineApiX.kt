package com.shuisechanggong.base.http

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext



fun<T> CoroutineScope.asyncApi(ioContext: CoroutineContext = Dispatchers.IO, api: suspend ()->T, callback:suspend (Response<T>)->Unit):Unit{
    var thisContext = this.coroutineContext
    launch {
        withContext(ioContext){
            supervisorScope{
                var response: Response<T>
                try {
                    response = Response.success(api())
                } catch (e: Exception) {
                    response = Response.fail(
                        NetworkException.from(e)
                    )
                }
                withContext(thisContext){
                    callback.invoke(response)
                }

            }
        }

    }
}