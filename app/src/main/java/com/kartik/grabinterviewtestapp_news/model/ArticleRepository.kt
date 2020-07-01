package com.kartik.grabinterviewtestapp_news.model

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.kartik.grabinterviewtestapp_news.model.dao.ArticleDAO
import com.kartik.grabinterviewtestapp_news.model.entities.Article

class ArticleRepository(application: Application) {
    private var articleDAO: ArticleDAO
    private var articles: LiveData<List<Article>>

    init {
        val articleDatabase = ArticleDatabase.getInstance(application)
        articleDAO = articleDatabase.articleDao()
        articles = articleDAO.getCachedArticles()
    }

    fun insertArticle(article: Article) {
        ArticleOperationAsyncTask(
            articleDAO,
            UtilConstants.INSERT_OPCODE
        ).execute(article)
    }

    fun updateArticle(article: Article) {
        ArticleOperationAsyncTask(
            articleDAO,
            UtilConstants.UPDATE_OPCODE
        ).execute(article)
    }

    fun deleteArticle(article: Article) {
        ArticleOperationAsyncTask(
            articleDAO,
            UtilConstants.DELETE_OPCODE
        ).execute(article)
    }

    fun clearCachedArticles() {
        ArticleOperationAsyncTask(
            articleDAO,
            UtilConstants.CLEARCACHE_OPCODE
        ).execute(null)
    }

    fun getCachedArticles(): LiveData<List<Article>> = articles

    private class ArticleOperationAsyncTask(val articleDAO: ArticleDAO, val opCode: Int): AsyncTask<Article, Unit, Unit>() {
        override fun doInBackground(vararg params: Article) {
            when(opCode) {
                UtilConstants.INSERT_OPCODE -> articleDAO.insertArticle(params[0])
                UtilConstants.UPDATE_OPCODE -> articleDAO.updateArticle(params[0])
                UtilConstants.DELETE_OPCODE -> articleDAO.deleteArticle(params[0])
                UtilConstants.CLEARCACHE_OPCODE -> articleDAO.clearCachedArticles()
            }
        }
    }

}