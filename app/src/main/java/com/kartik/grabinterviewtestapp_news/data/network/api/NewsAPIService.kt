package com.kartik.grabinterviewtestapp_news.data.network.api

import com.kartik.grabinterviewtestapp_news.data.network.responses.ResponseModel
import retrofit2.Call
import retrofit2.http.GET

interface NewsAPIService {

    @GET("v2/top-headlines?language=en")
    fun getWorldwideTopHeadlines(): Call<ResponseModel.ArticleList>

    @GET("v2/top-headlines?country=in&language=en")
    fun getIndiaTopHeadlines(): Call<ResponseModel.ArticleList>
}