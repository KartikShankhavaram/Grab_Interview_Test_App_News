package com.kartik.grabinterviewtestapp_news.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.kartik.grabinterviewtestapp_news.model.entities.Article

@Dao
interface ArticleDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertArticle(article: Article)

    @Insert
    fun insertArticles(articles: List<Article>)

    @Query("DELETE from articles")
    fun clearCachedArticles()

    @Query("SELECT * from articles")
    fun getCachedArticles(): LiveData<List<Article>>

    @RawQuery
    fun executeQueryAndReturnArticles(query: SupportSQLiteQuery): List<Article>
}