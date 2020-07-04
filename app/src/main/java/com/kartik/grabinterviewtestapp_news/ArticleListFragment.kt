package com.kartik.grabinterviewtestapp_news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.kartik.grabinterviewtestapp_news.ui.ArticleRecyclerViewAdapter
import com.kartik.grabinterviewtestapp_news.ui.ArticleViewModel
import kotlinx.android.synthetic.main.fragment_article_list.view.*

class ArticleListFragment() : Fragment() {

    var recyclerViewAdapter: ArticleRecyclerViewAdapter? = null
    private val articleViewModel by activityViewModels<ArticleViewModel>()

    companion object {
        fun newInstance(): ArticleListFragment {
            return ArticleListFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (recyclerViewAdapter == null) recyclerViewAdapter = ArticleRecyclerViewAdapter(context, activity as MainActivity, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_article_list, container, false)
        rootView.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerViewAdapter
        }
        articleViewModel.getArticleList().observe(viewLifecycleOwner, Observer {
            (recyclerViewAdapter as ArticleRecyclerViewAdapter).setArticleList(it)
        })
        articleViewModel.getLoadingStatus().observe(viewLifecycleOwner, Observer {
            rootView.swipeRefreshLayout.isRefreshing = it
        })
        rootView.swipeRefreshLayout.setOnRefreshListener {
            articleViewModel.refreshArticles()
        }
        return rootView
    }
}