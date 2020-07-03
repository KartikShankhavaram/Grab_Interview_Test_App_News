package com.kartik.grabinterviewtestapp_news.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kartik.grabinterviewtestapp_news.data.database.entities.Article
import com.kartik.grabinterviewtestapp_news.data.database.dao.ArticleDAO

@Database(entities = [Article::class], version = 1)
abstract class ArticleDatabase: RoomDatabase() {

    abstract fun articleDao(): ArticleDAO

    companion object {
        private var INSTANCE: ArticleDatabase? = null

        @Synchronized
        fun getInstance(context: Context): ArticleDatabase {
            if(INSTANCE == null) {
                INSTANCE = Room
                    .databaseBuilder(context.applicationContext, ArticleDatabase::class.java, "Article Database")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE as ArticleDatabase
        }
    }

}