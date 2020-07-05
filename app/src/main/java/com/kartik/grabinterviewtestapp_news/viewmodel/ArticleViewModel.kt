package com.kartik.grabinterviewtestapp_news.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kartik.grabinterviewtestapp_news.data.ArticleRepository
import com.kartik.grabinterviewtestapp_news.data.database.entities.Article
import com.kartik.grabinterviewtestapp_news.data.network.utils.NetworkUtils

class ArticleViewModel(application: Application): AndroidViewModel(application) {

    private var repository: ArticleRepository = ArticleRepository(application)
    private var articleList: LiveData<List<Article>>
    private var networkConnectionStatus: LiveData<Boolean>
    private var fetchingFromNetwork: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    init {
        repository.setFetchingFromNetworkLiveData(fetchingFromNetwork)
        articleList = repository.getArticles()
        NetworkUtils.apply {
            init(application.applicationContext)
            networkConnectionStatus = getNetworkStatus()
        }
    }

    fun getArticleList(): LiveData<List<Article>> = articleList

    fun refreshArticles() = repository.getArticles()

    fun getNetworkStatus(): LiveData<Boolean> = networkConnectionStatus

    fun getLoadingStatus(): LiveData<Boolean> = fetchingFromNetwork
}