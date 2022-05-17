package com.android.takit.data.retmote

import com.android.takit.data.local.TakitAuthSharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    private const val BASE_URL = "http://ec2-3-38-246-146.ap-northeast-2.compute.amazonaws.com:8080/api/"

    private val gson: Gson = GsonBuilder().setLenient().create()

    private fun provideInterceptor() = Interceptor { chain ->
        with(chain) {
            proceed(
                request()
                    .newBuilder()
                    .addHeader(
                        "x-auth-token",
                        TakitAuthSharedPreferences.getAccessToken()
                    )
                    .build()
            )
        }
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(provideOkHttpClient(provideInterceptor()))
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()


    private fun provideOkHttpClient(interceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
            )
            .addInterceptor(interceptor)
            .build()
    }

    val takitService : TakitService = retrofit.create(TakitService::class.java)


}