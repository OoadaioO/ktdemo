package com.shuisechanggong.ktdemo.datamodel

import androidx.annotation.Keep

@Keep
data class ImageBean(val imageUrl:String,val imageSize:String,val imageFileLength:Long) {

}