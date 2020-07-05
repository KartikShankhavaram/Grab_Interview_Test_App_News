package com.kartik.grabinterviewtestapp_news.ui.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kartik.grabinterviewtestapp_news.ui.fragments.ArticleListFragment
import com.kartik.grabinterviewtestapp_news.R
import com.kartik.grabinterviewtestapp_news.data.database.entities.Article
import com.kartik.grabinterviewtestapp_news.ui.utils.FragmentSwitcher
import com.kartik.grabinterviewtestapp_news.ui.utils.UtilFunctions
import kotlinx.android.synthetic.main.news_cardview.view.*

class ArticleRecyclerViewAdapter(val context: Context?, val listener: FragmentSwitcher, val fragment: ArticleListFragment) :
    RecyclerView.Adapter<ArticleRecyclerViewAdapter.ArticleViewHolder>() {

    private var articles: List<Article> = arrayListOf()

    fun setArticleList(articles: List<Article>) {
        this.articles = articles
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder =
        ArticleViewHolder(
            LayoutInflater.from(context), parent, context, listener, fragment
        )

    override fun getItemCount(): Int = articles.size

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    class ArticleViewHolder(
        private val inflater: LayoutInflater,
        private val parent: ViewGroup,
        val context: Context?,
        val listener: FragmentSwitcher,
        val fragment: ArticleListFragment
    ) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.news_cardview, parent, false)) {

        private val titleView: TextView = itemView.news_title_textview
        private val sourceView: TextView = itemView.news_source_textview
        private val timestampView: TextView = itemView.timestamp_textview
        private val newsImageView: ImageView = itemView.news_imageview
        val articleItemView: ConstraintLayout = itemView.articleItemView


        fun bind(article: Article) {
            titleView.text = article.title
            sourceView.text = article.sourceName
            timestampView.text = UtilFunctions.getRelativeTimeSpanString(article.publishedAt)
//            val requestOptions = RequestOptions
            if (context != null)
                Glide.with(context).load(article.urlToImage).into(newsImageView)
            articleItemView.setOnClickListener {
                listener.switchFragment(article, itemView)
            }
        }
    }

}