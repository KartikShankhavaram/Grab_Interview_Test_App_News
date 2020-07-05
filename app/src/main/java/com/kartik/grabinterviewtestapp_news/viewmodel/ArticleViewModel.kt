package com.kartik.grabinterviewtestapp_news.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.kartik.grabinterviewtestapp_news.data.ArticleRepository
import com.kartik.grabinterviewtestapp_news.data.OperationStatus
import com.kartik.grabinterviewtestapp_news.data.database.entities.Article
import com.kartik.grabinterviewtestapp_news.data.network.utils.NetworkUtils

class ArticleViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: ArticleRepository = ArticleRepository(application)
    private var articleList: LiveData<List<Article>>
    private var networkConnectionStatus: LiveData<Boolean>
    private var operationStatus: LiveData<OperationStatus>

    init {
        operationStatus = repository.getOperationStatus()
        articleList = repository.getArticles()
        networkConnectionStatus = NetworkUtils.getNetworkStatus()
    }

    fun getArticleList(): LiveData<List<Article>> = articleList

    fun refreshArticles() = repository.getArticles()

    fun getNetworkStatus(): LiveData<Boolean> = networkConnectionStatus

    fun getOperationStatus(): LiveData<OperationStatus> = operationStatus
}