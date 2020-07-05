package com.kartik.grabinterviewtestapp_news.ui.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Slide
import com.kartik.grabinterviewtestapp_news.R
import com.kartik.grabinterviewtestapp_news.ui.activities.MainActivity
import com.kartik.grabinterviewtestapp_news.ui.recyclerview.ArticleRecyclerViewAdapter
import com.kartik.grabinterviewtestapp_news.viewmodel.ArticleViewModel
import kotlinx.android.synthetic.main.fragment_article_list.view.*

class ArticleListFragment() : Fragment() {

    var recyclerViewAdapter: ArticleRecyclerViewAdapter? = null
    private val articleViewModel by activityViewModels<ArticleViewModel>()
    private val TAG = "ArticleListFragment"

    companion object {
        fun newInstance(): ArticleListFragment {
            return ArticleListFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (recyclerViewAdapter == null) recyclerViewAdapter =
            ArticleRecyclerViewAdapter(
                context,
                activity as MainActivity,
                this
            )
        exitTransition = Slide(Gravity.START)
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
        val customToolbar = inflater.inflate(R.layout.main_toolbar, null)
        val actionBar = (activity as MainActivity).supportActionBar
        actionBar?.apply {
            customView = customToolbar
            setDisplayShowCustomEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        articleViewModel.getArticleList().observe(viewLifecycleOwner, Observer {
            (recyclerViewAdapter as ArticleRecyclerViewAdapter).setArticleList(it)
        })
        articleViewModel.getOperationStatus().observe(viewLifecycleOwner, Observer {
            setLoading(rootView, it.loading)
            if (it.t?.message != null)
                setError(it.t.message!!)
        })
        rootView.swipeRefreshLayout.setOnRefreshListener {
            articleViewModel.refreshArticles()
        }
        return rootView
    }

    private fun setLoading(rootView: View, status: Boolean) {
        rootView.swipeRefreshLayout.isRefreshing = status
    }

    private fun setError(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}