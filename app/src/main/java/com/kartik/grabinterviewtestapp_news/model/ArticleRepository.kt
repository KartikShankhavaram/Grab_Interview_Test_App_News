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

    fun insertArticles(articles: List<Article>) {
        ArticleOperationAsyncTask(
            articleDAO,
            UtilConstants.INSERT_MULTIPLE_OPCODE
        ).execute(*articles.toTypedArray())
    }

    fun clearCachedArticles() {
        ArticleOperationAsyncTask(
            articleDAO,
            UtilConstants.CLEARCACHE_OPCODE
        ).execute(null)
    }

    fun getCachedArticles(): LiveData<List<Article>> = articles

    private class ArticleOperationAsyncTask(val articleDAO: ArticleDAO, val opCode: Int) :
        AsyncTask<Article, Unit, Unit>() {
        override fun doInBackground(vararg params: Article) {
            when (opCode) {
                UtilConstants.INSERT_OPCODE -> articleDAO.insertArticle(params[0])
                UtilConstants.INSERT_MULTIPLE_OPCODE -> articleDAO.insertArticles(params.asList())
                UtilConstants.CLEARCACHE_OPCODE -> articleDAO.clearCachedArticles()
            }
        }
    }
}