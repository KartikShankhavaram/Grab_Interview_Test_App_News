package com.kartik.grabinterviewtestapp_news.data.network.api

import android.content.Context

class NewsAPIUtils {
    companion object {

        private val BASE_URL = "https://newsapi.org/"

        fun getNewsAPIService(mContext: Context): NewsAPIService = RetrofitClient.getClient(BASE_URL, mContext)!!.create(NewsAPIService::class.java)
    }
}