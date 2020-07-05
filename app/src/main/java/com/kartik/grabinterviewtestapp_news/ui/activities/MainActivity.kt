package com.kartik.grabinterviewtestapp_news.ui.activities

import android.content.ComponentName
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.core.view.ViewCompat
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.transition.Slide
import com.kartik.grabinterviewtestapp_news.ui.fragments.ArticleDescriptionFragment
import com.kartik.grabinterviewtestapp_news.ui.fragments.ArticleListFragment
import com.kartik.grabinterviewtestapp_news.R
import com.kartik.grabinterviewtestapp_news.data.database.entities.Article
import com.kartik.grabinterviewtestapp_news.ui.recyclerview.ArticleRecyclerViewAdapter
import com.kartik.grabinterviewtestapp_news.viewmodel.ArticleViewModel
import com.kartik.grabinterviewtestapp_news.ui.utils.FragmentSwitcher
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.news_cardview.view.*

class MainActivity : AppCompatActivity(),
    FragmentSwitcher {

    var customTabsClient: CustomTabsClient? = null
    private val TAG = "[Deb]MainActivity"
    private lateinit var articleViewModel: ArticleViewModel
    var adapter: ArticleRecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        articleViewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)

        articleViewModel.getNetworkStatus().observe(this, Observer {
            if (it) networkStatus.visibility = View.GONE
            else networkStatus.visibility = View.VISIBLE
        })

        if (savedInstanceState == null)
            supportFragmentManager.commit {
                replace(
                    R.id.main_frame,
                    ArticleListFragment.newInstance()
                )
            }

    }

    override fun onStart() {
        super.onStart()
        val ok = CustomTabsClient.bindCustomTabsService(this, packageName, object : CustomTabsServiceConnection() {
            override fun onCustomTabsServiceConnected(name: ComponentName, client: CustomTabsClient) {
                customTabsClient = client
                client.warmup(0)
                Log.d(TAG, "onCustomTabsServiceConnected: connected")
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                customTabsClient = null
                Log.d(TAG, "onServiceDisconnected: disconnected")
            }
        })
        Log.d(TAG, "onStart: customTabServiceConnected: $ok")
    }

    override fun switchFragment(
        selectedArticle: Article,
        itemView: View
    ) {
        ViewCompat.setTransitionName(itemView.news_imageview, "desc_imageview")
        ViewCompat.setTransitionName(itemView.news_title_textview, "news_title")
        supportFragmentManager.commit {
            Log.d(TAG, "switchFragment: $selectedArticle")
            val fragment =
                ArticleDescriptionFragment.newInstance(
                    selectedArticle
                )
            fragment.apply {
                setReorderingAllowed(true)
                enterTransition =  Slide(Gravity.END)
                exitTransition =  Slide(Gravity.START)
            }
            addSharedElement(itemView.news_imageview, "desc_imageview")
            addSharedElement(itemView.news_title_textview, "news_title")
            replace(R.id.main_frame, fragment)
            addToBackStack("articleList")
        }
    }
}