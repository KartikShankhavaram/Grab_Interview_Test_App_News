package com.kartik.grabinterviewtestapp_news.data.database.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.kartik.grabinterviewtestapp_news.data.database.entities.Article

@Dao
abstract class ArticleDAO {

//    @Insert(onConflict = OnConflictStrategy.ABORT)
//    fun insertArticle(article: Article)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertArticles(vararg articles: Article)

    @Query("DELETE from articles")
    abstract fun clearCachedArticles()

    @Query("SELECT * from articles")
    abstract fun getCachedArticles(): LiveData<List<Article>>

    @Transaction
    open fun setNewCachedArticles(articles: List<Article>) {
        clearCachedArticles()
        insertArticles(*articles.toTypedArray())
    }

    @RawQuery
    abstract fun executeQueryAndReturnArticles(query: SupportSQLiteQuery): List<Article>
}