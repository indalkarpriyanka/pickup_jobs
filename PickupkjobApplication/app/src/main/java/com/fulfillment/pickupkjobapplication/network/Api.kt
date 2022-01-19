package com.fulfillment.pickupkjobapplication.network


import android.util.Log
import com.fulfillment.pickupkjobapplication.utils.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object Api {
    private const val BASE_URL = "https://api-gateway-wdlsgf2z3a-ew.a.run.app/"
    private val httpClient: OkHttpClient =
        OkHttpClient.Builder().connectTimeout(5, TimeUnit.MINUTES).writeTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .apply {
                addInterceptor(Interceptor { chain ->
                    val builder = chain.request().newBuilder()
                    builder.addHeader("Authorization", "Bearer " + Constants.USER_TOKEN)
                        .addHeader("Content-Type", "application/json")

                    Log.d("Auth", Constants.USER_TOKEN)

                    return@Interceptor chain.proceed(builder.build())
                })
            }.build()
    private var retrofit: Retrofit =
        Retrofit.Builder()
            .client(httpClient).addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL).build()
    val retrofitService: PickupJobService by lazy {
        retrofit.create(PickupJobService::class.java)
    }
}

