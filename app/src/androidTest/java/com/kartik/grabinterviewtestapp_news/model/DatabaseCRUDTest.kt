package com.kartik.grabinterviewtestapp_news.model

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kartik.grabinterviewtestapp_news.model.dao.ArticleDAO
import com.kartik.grabinterviewtestapp_news.utils.MockArticleGenerator
import com.kartik.grabinterviewtestapp_news.utils.getOrAwaitValue
import org.junit.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseCRUDTest {
    private lateinit var articleDAO: ArticleDAO
    private lateinit var articleDatabase: ArticleDatabase

    private val TAG = "DatabaseCRUDTest"

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        // Get Context
        val context = ApplicationProvider.getApplicationContext<Context>()

        // Setup Database
        articleDatabase = Room.inMemoryDatabaseBuilder(context, ArticleDatabase::class.java).build()
        //Get DAO
        articleDAO = articleDatabase.articleDao()
    }

    @Test
    fun insertArticleTest() {
        // Get mock article
        val article = MockArticleGenerator.getMockArticles()[0]
        // Insert article
        articleDAO.insertArticle(article)
        // Get Article
        val dbArticle = articleDAO.executeQueryAndReturnArticles(SimpleSQLiteQuery("SELECT * FROM articles"))[0]
        assertTrue(article == dbArticle)
    }

    @Test
    fun insertArticlesTest() {
        //Get mock Articles
        val articles = MockArticleGenerator.getMockArticles(10)
        // Insert articles
        articleDAO.insertArticles(articles)
        // Get Articles
        val dbArticles = articleDAO.executeQueryAndReturnArticles(SimpleSQLiteQuery("SELECT * FROM articles"))
        assertTrue(dbArticles.sortedBy { it.title } == articles.sortedBy { it.title })
    }

    @Test
    fun clearCachedArticlesTest() {
        // Get mock articles
        val articles = MockArticleGenerator.getMockArticles(10)
        // Insert articles
        articleDAO.insertArticles(articles)
        // Check size of current database
        val originalSize = articleDAO.executeQueryAndReturnArticles(SimpleSQLiteQuery("SELECT * FROM articles")).size
        // Clear cached articles
        articleDAO.clearCachedArticles()
        // CHeck size of cleared database
        val finalSize = articleDAO.executeQueryAndReturnArticles(SimpleSQLiteQuery("SELECT * FROM articles")).size
        assertTrue(originalSize > finalSize && finalSize == 0)
    }

    @Test
    fun getCachedArticlesTest() {
        // Get mock articles
        val articles = MockArticleGenerator.getMockArticles(10)
        // Insert articles
        articleDAO.insertArticles(articles)
        val dbArticles = articleDAO.getCachedArticles().getOrAwaitValue()
        assertTrue(dbArticles.sortedBy { it.title } == articles.sortedBy { it.title })
    }

    @After
    fun teardown() {
        articleDatabase.close()
    }
}