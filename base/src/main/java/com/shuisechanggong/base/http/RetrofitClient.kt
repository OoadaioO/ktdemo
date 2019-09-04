package com.shuisechanggong.base.http

import okhttp3.ConnectionSpec
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import retrofit2.Retrofit
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.X509TrustManager
import kotlin.reflect.KClass

/**
 *
 */


object RetrofitClient {

    private var retrofit: Retrofit? = null
    private val TIME_OUT =1_000L

    fun init(
        baseUrl: String,
        interceptors: List<Interceptor>? = null,
        networkInterceptors: List<Interceptor>? = null,
        enableSsl: Boolean = false
    ) {
        val okHttpBuilder = OkHttpClient.Builder()
        if (!enableSsl) {
            val trustManager = object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
                override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
                override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            }
            val trustAllCerts = arrayOf(
                trustManager
            )

            val hv = object : HostnameVerifier {
                override fun verify(hostname: String?, session: SSLSession?): Boolean = true
            }
            val sc = SSLContext.getInstance("SSL")
            sc.init(null, trustAllCerts, SecureRandom())
            okHttpBuilder.sslSocketFactory(sc.socketFactory, trustManager)
            val cs = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .build()
            okHttpBuilder.connectionSpecs(
                arrayListOf(
                    cs
                )
            )

        }

        interceptors?.forEach {
            okHttpBuilder.addInterceptor(it)
        }
        networkInterceptors?.forEach {
            okHttpBuilder.addNetworkInterceptor(it)
        }
        okHttpBuilder.connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
        okHttpBuilder.readTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
        okHttpBuilder.writeTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpBuilder.build())
            .addConverterFactory(ResultConvertFactory.create())
            .build()
    }

    fun <T> create(cls: Class<T>): T = retrofit?.create(cls) ?: throw IllegalArgumentException("初始化服务失败")
    fun <T : Any> create(cls: KClass<T>): T = retrofit?.create(cls.java) ?: throw IllegalArgumentException("初始化服务失败")

}