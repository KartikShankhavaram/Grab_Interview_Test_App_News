package com.kartik.grabinterviewtestapp_news.data.database.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "articles")
@Parcelize
data class Article(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name = "source_id") var sourceId: String?,
    @ColumnInfo(name = "source_name") var sourceName: String?,
    @ColumnInfo(name = "author") var author: String?,
    @ColumnInfo(name = "title") var title: String?,
    @ColumnInfo(name = "description") var desc: String?,
    @ColumnInfo(name = "url") var url: String?,
    @ColumnInfo(name = "url_to_image") var urlToImage: String?,
    @ColumnInfo(name = "published_at") var publishedAt: String?,
    @ColumnInfo(name = "content") var content: String?
) : Parcelable {
    constructor(
        sourceId: String?,
        sourceName: String?,
        author: String?,
        title: String?,
        desc: String?,
        url: String?,
        urlToImage: String?,
        publishedAt: String?,
        content: String?
    ) : this(null, sourceId, sourceName, author, title, desc, url, urlToImage, publishedAt, content)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Article

        if (sourceId != other.sourceId) return false
        if (sourceName != other.sourceName) return false
        if (author != other.author) return false
        if (title != other.title) return false
        if (desc != other.desc) return false
        if (url != other.url) return false
        if (urlToImage != other.urlToImage) return false
        if (publishedAt != other.publishedAt) return false
        if (content != other.content) return false

        return true
    }

    override fun hashCode(): Int {
        var result = sourceId?.hashCode() ?: 0
        result = 31 * result + (sourceName?.hashCode() ?: 0)
        result = 31 * result + (author?.hashCode() ?: 0)
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (desc?.hashCode() ?: 0)
        result = 31 * result + (url?.hashCode() ?: 0)
        result = 31 * result + (urlToImage?.hashCode() ?: 0)
        result = 31 * result + (publishedAt?.hashCode() ?: 0)
        result = 31 * result + (content?.hashCode() ?: 0)
        return result
    }

}