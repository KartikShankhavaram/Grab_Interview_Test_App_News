package com.kartik.grabinterviewtestapp_news.utils

import com.kartik.grabinterviewtestapp_news.data.database.entities.Article

class MockArticleGenerator {
    companion object {
        fun getMockArticles(count: Int = 1): List<Article> {
            val list = arrayListOf<Article>()
            for (i in 1..count) {
                val article = Article(
                    "sid${i / 10}",
                    "sName${i / 10}",
                    "author${i / 4}",
                    "title$i",
                    "desc$",
                    "url$i",
                    "urltoimg$i",
                    "publdate$i",
                    "content$i"
                )
                list.add(article)
            }
            return list
        }


    }
}