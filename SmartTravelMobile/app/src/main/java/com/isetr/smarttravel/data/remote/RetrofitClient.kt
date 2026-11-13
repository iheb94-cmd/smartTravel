package com.isetr.smarttravel.data.remote

import android.content.Context
import com.isetr.smarttravel.data.local.SessionManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    //const val BASE_URL = "http://10.0.2.2:8081/"
    const val BASE_URL = "http://192.168.1.2:8081/"
    //const val BASE_URL = "http://192.168.137.1:8081/"


    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val token = if (::appContext.isInitialized)
                    SessionManager(appContext).getToken()
                else null

                val request = chain.request().newBuilder().apply {
                    if (!token.isNullOrEmpty()) addHeader("Authorization", "Bearer $token")
                }.build()
                chain.proceed(request)
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val authApi: AuthApi by lazy { retrofit.create(AuthApi::class.java) }

    val offerApi: OfferApi by lazy { retrofit.create(OfferApi::class.java) }
}