package com.kartik.grabinterviewtestapp_news.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kartik.grabinterviewtestapp_news.model.entities.Article

@Dao
interface ArticleDAO {

    @Insert
    fun insertArticle(article: Article)

    @Update
    fun updateArticle(article: Article)

    @Delete
    fun deleteArticle(article: Article)

    @Query("DELETE from articles")
    fun clearCachedArticles()

    @Query("SELECT * from articles")
    fun getCachedArticles(): LiveData<List<Article>>
}