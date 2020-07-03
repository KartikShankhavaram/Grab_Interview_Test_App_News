package com.kartik.grabinterviewtestapp_news.data

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import com.kartik.grabinterviewtestapp_news.data.database.ArticleDatabase
import com.kartik.grabinterviewtestapp_news.data.database.dao.ArticleDAO
import com.kartik.grabinterviewtestapp_news.data.database.entities.Article
import com.kartik.grabinterviewtestapp_news.data.network.api.NewsAPIService
import com.kartik.grabinterviewtestapp_news.data.network.api.NewsAPIUtils
import com.kartik.grabinterviewtestapp_news.data.network.responses.ResponseModel
import com.kartik.grabinterviewtestapp_news.data.network.utils.NetworkUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticleRepository(application: Application) {
    private var articleDAO: ArticleDAO
    private var articles: LiveData<List<Article>>
    private var newsAPIService: NewsAPIService
    private var networkConnected: LiveData<Boolean>

    private val TAG = "[Deb]ArticleRepository"

    init {
        val articleDatabase =
            ArticleDatabase.getInstance(
                application
            )
        articleDAO = articleDatabase.articleDao()
        articles = articleDAO.getCachedArticles()

        newsAPIService = NewsAPIUtils.getNewsAPIService(application.applicationContext)

        NetworkUtils.init(application.applicationContext)
        networkConnected = NetworkUtils.getNetworkStatus()
    }

    fun getArticles(): LiveData<List<Article>> {
        newsAPIService.getIndiaTopHeadlines().enqueue(object : Callback<ResponseModel.ArticleList> {
            override fun onFailure(call: Call<ResponseModel.ArticleList>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.localizedMessage}")
            }

            override fun onResponse(
                call: Call<ResponseModel.ArticleList>,
                response: Response<ResponseModel.ArticleList>
            ) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    Log.d(TAG, "onResponse: $body")
                    val dbList: List<Article> = transformNetworkResponseToRoom(body)
                    setNewCachedArticles(dbList)
                }
            }
        })
        return articles
    }

    private fun transformNetworkResponseToRoom(body: ResponseModel.ArticleList): List<Article> {
        val dbList = arrayListOf<Article>()
        for (article: ResponseModel.Article in body.articles) {
            dbList.add(
                Article(
                    article.source.id,
                    article.source.name,
                    article.author,
                    article.title,
                    article.description,
                    article.url,
                    article.urlToImage,
                    article.publishedAt,
                    article.content
                )
            )
        }
        return dbList
    }

//    private fun insertArticle(article: Article) {
//        ArticleOperationAsyncTask(
//            articleDAO,
//            INSERT_OPCODE
//        ).execute(article)
//    }

    private fun setNewCachedArticles(articles: List<Article>) {
        ArticleOperationAsyncTask(articleDAO).execute(*articles.toTypedArray())
    }

//    private fun insertArticles(articles: List<Article>) {
////        ArticleOperationAsyncTask(
////            articleDAO,
////            INSERT_MULTIPLE_OPCODE
////        ).execute(*articles.toTypedArray())
////    }
////
////    private fun clearCachedArticles() {
////        ArticleOperationAsyncTask(
////            articleDAO,
////            CLEARCACHE_OPCODE
////        ).execute(null)
////    }

//    private fun getCachedArticles(): LiveData<List<Article>> = articles

    private class ArticleOperationAsyncTask(
        val articleDAO: ArticleDAO
    ) :
        AsyncTask<Article, Unit, Unit>() {
        override fun doInBackground(vararg params: Article) {
            articleDAO.setNewCachedArticles(params.asList())
        }
    }
}