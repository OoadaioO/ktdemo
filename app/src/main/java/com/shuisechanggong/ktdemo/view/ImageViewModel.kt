package com.shuisechanggong.ktdemo.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shuisechanggong.base.BaseViewModel
import com.shuisechanggong.base.http.Response
import com.shuisechanggong.base.http.RetrofitClient
import com.shuisechanggong.base.http.asyncApi
import com.shuisechanggong.ktdemo.datamodel.ImageBean
import com.shuisechanggong.ktdemo.repository.ApiService

/**
 *
 */

class ImageViewModel : BaseViewModel() {

    val api: ApiService by lazy { RetrofitClient.create(ApiService::class.java) }
    val requestLiveData: MutableLiveData<Response<List<ImageBean>>> = MutableLiveData()
    val loadLiveData: MutableLiveData<Response<List<ImageBean>>> = MutableLiveData()


    init {
        request()
    }


    fun request() {
        viewModelScope.asyncApi(api = { api.imageListRandom() }) {
            requestLiveData.value = it
        }
    }

    fun load() {
        viewModelScope.asyncApi(api = { api.imageListRandom() }) {
            loadLiveData.value = it
        }
    }

}