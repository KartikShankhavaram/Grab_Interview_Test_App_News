package com.kartik.grabinterviewtestapp_news.data.network.responses


import com.google.gson.annotations.SerializedName

object ResponseModel {
    data class ArticleList(
        @SerializedName("articles")
        var articles: List<Article>,
        @SerializedName("status")
        var status: String,
        @SerializedName("totalResults")
        var totalResults: Int
    )

    data class Article(
        @SerializedName("author")
        var author: String,
        @SerializedName("content")
        var content: String,
        @SerializedName("description")
        var description: String,
        @SerializedName("publishedAt")
        var publishedAt: String,
        @SerializedName("source")
        var source: Source,
        @SerializedName("title")
        var title: String,
        @SerializedName("url")
        var url: String,
        @SerializedName("urlToImage")
        var urlToImage: String
    )

    data class Source(
        @SerializedName("id")
        var id: String,
        @SerializedName("name")
        var name: String
    )
}