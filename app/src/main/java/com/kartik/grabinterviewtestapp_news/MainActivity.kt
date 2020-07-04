package com.kartik.grabinterviewtestapp_news

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.transition.Slide
import com.kartik.grabinterviewtestapp_news.data.database.entities.Article
import com.kartik.grabinterviewtestapp_news.ui.ArticleRecyclerViewAdapter
import com.kartik.grabinterviewtestapp_news.ui.ArticleViewModel
import com.kartik.grabinterviewtestapp_news.ui.FragmentSwitcher
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.news_cardview.view.*

class MainActivity : AppCompatActivity(), FragmentSwitcher {

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

        supportFragmentManager.commit {
            replace(R.id.main_frame, ArticleListFragment.newInstance())
        }

    }

    override fun switchFragment(
        selectedArticle: Article,
        currentFragment: Fragment,
        itemView: View
    ) {
        ViewCompat.setTransitionName(itemView.news_imageview, "desc_imageview")
        ViewCompat.setTransitionName(itemView.news_title_textview, "news_title")
        supportFragmentManager.commit {
            Log.d(TAG, "switchFragment: $selectedArticle")
            val fragment = ArticleDescriptionFragment.newInstance(selectedArticle)
            fragment.apply {
                setReorderingAllowed(true)
                enterTransition = Slide()
            }
            currentFragment.exitTransition = Slide()
            addSharedElement(itemView.news_imageview, "desc_imageview")
            addSharedElement(itemView.news_title_textview, "news_title")
            replace(R.id.main_frame, fragment)
            addToBackStack("articleList")
        }
    }
}