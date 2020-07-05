package com.kartik.grabinterviewtestapp_news

import android.app.Application
import com.kartik.grabinterviewtestapp_news.data.network.utils.NetworkUtils

class NewsApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        NetworkUtils.init(this)
    }

}