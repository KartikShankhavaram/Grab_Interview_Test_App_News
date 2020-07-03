package com.kartik.grabinterviewtestapp_news.data.network.api

import android.content.Context
import com.kartik.grabinterviewtestapp_news.R
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient {
    companion object {
        private var INSTANCE: Retrofit? = null

        fun getClient(baseUrl: String, mContext: Context): Retrofit? {
            if (INSTANCE == null) {
                val builder = OkHttpClient.Builder()
                builder.addInterceptor { chain ->
                    val original = chain.request()
                    val apiKey = mContext.resources.getString(R.string.api_key)
                    val requestBuilder = original.newBuilder().header("X-Api-Key", apiKey)
                    chain.proceed(requestBuilder.build())
                }
                builder.connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS)
                INSTANCE = Retrofit.Builder().baseUrl(baseUrl).client(builder.build())
                    .addConverterFactory(GsonConverterFactory.create()).build()
            }
            return INSTANCE
        }
    }
}