package com.kartik.grabinterviewtestapp_news.data

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

class ArticleRepository(val application: Application) {
    private var articleDAO: ArticleDAO
    private var operationStatus: MutableLiveData<OperationStatus> = MutableLiveData()
    private var articles: LiveData<List<Article>>
    private var newsAPIService: NewsAPIService
    private var networkConnected: Boolean = true
    private val TAG = "[Deb]ArticleRepository"

    init {
        val articleDatabase =
            ArticleDatabase.getInstance(
                application
            )
        articleDAO = articleDatabase.articleDao()
        articles = articleDAO.getCachedArticles()
        newsAPIService = NewsAPIUtils.getNewsAPIService(application.applicationContext)

        NetworkUtils.getNetworkStatus().observeForever {
            networkConnected = it
        }
    }

    fun getOperationStatus(): LiveData<OperationStatus> = operationStatus

    fun getArticles(): LiveData<List<Article>> {
        operationStatus.postValue(OperationStatus(null, true))
        if (networkConnected) {
            newsAPIService.getIndiaTopHeadlines()
                .enqueue(object : Callback<ResponseModel.ArticleList> {
                    override fun onFailure(call: Call<ResponseModel.ArticleList>, t: Throwable) {
                        Log.d(TAG, "onFailure: ${t.localizedMessage}")
                        operationStatus.postValue(
                            OperationStatus(
                                NetworkResponseException("Could not fetch news"),
                                false
                            )
                        )
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
                            operationStatus.postValue(OperationStatus(null, false))
                        } else {
                            operationStatus.postValue(
                                OperationStatus(
                                    NetworkResponseException("Error in fetching news"),
                                    false
                                )
                            )
                        }
                    }
                })
        } else {
            operationStatus.postValue(
                OperationStatus(
                    NoNetworkException("No network connection"),
                    false
                )
            )
        }
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

    private fun setNewCachedArticles(articles: List<Article>) {
        Executor.IOThread {
            articleDAO.setNewCachedArticles(articles)
        }
    }
}

data class OperationStatus(val t: Throwable?, val loading: Boolean) {}
class NoNetworkException(val msg: String) : Exception(msg) {}
class NetworkResponseException(val msg: String) : Exception(msg) {}
