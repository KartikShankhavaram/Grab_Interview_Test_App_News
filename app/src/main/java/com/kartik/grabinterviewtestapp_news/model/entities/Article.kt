package com.kartik.grabinterviewtestapp_news.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class Article(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "source_id") var sourceId: String?,
    @ColumnInfo(name = "source_name") var sourceName: String?,
    @ColumnInfo(name = "author") var author: String?,
    @ColumnInfo(name = "title") var title: String?,
    @ColumnInfo(name = "description") var desc: String?,
    @ColumnInfo(name = "url") var url: String?,
    @ColumnInfo(name = "url_to_image") var urlToImage: String?,
    @ColumnInfo(name = "published_at") var publishedAt: String?,
    @ColumnInfo(name = "content") var content: String?
)