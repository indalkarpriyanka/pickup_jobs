package com.fulfillment.pickupkjobapplication.network


import android.util.Log
import com.fulfillment.pickupkjobapplication.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit


object Api {

    private const val BASE_URL = "https://api-gateway-wdlsgf2z3a-ew.a.run.app/"
    private const val AUTHORIZATION_KEY = "Bearer F19eTsqiL9TPCZylSbrT57DnVj03"

    private val httpClient: OkHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(Interceptor { chain ->
            val builder = chain.request().newBuilder()
            builder.header("Authorization", "Bearer "+Constants.USER_TOKEN)
            return@Interceptor chain.proceed(builder.build())
        })
    }.build()

//    private val moshi: Moshi = Moshi.Builder()
//        .add(MovieModelAdapter())
//        .addLast(KotlinJsonAdapterFactory())
//        .build()

    private var retrofit: Retrofit =
        Retrofit.Builder()
            .client(httpClient)
            .baseUrl(BASE_URL).build()

    val retrofitService: PickupJobService by lazy {
        retrofit.create(PickupJobService::class.java)
    }
}

