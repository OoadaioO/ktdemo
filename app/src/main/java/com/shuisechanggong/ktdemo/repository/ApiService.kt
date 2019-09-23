package com.shuisechanggong.ktdemo.repository

import com.shuisechanggong.ktdemo.datamodel.ImageBean
import com.shuisechanggong.ktdemo.datamodel.JokeBean
import com.shuisechanggong.ktdemo.datamodel.JokeResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *
 */

interface ApiService {

    @GET("jokes/list")
    suspend fun jokeList(@Query("page") page: Int): JokeResponse


    @GET("jokes/list/random")
    suspend fun jokeListRandom():List<JokeBean>


    @GET("image/girl/list/random")
    suspend fun imageListRandom():List<ImageBean>

}
