package com.kartik.grabinterviewtestapp_news

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.kartik.grabinterviewtestapp_news.data.database.entities.Article
import com.kartik.grabinterviewtestapp_news.ui.ArticleViewModel
import com.kartik.grabinterviewtestapp_news.ui.utils.UtilFunctions
import kotlinx.android.synthetic.main.fragment_article_description.view.*

class ArticleDescriptionFragment() : Fragment() {
    private lateinit var lifecycleOwner: LifecycleOwner
    private val articleViewModel by activityViewModels<ArticleViewModel>()
    private lateinit var article: Article

    companion object {
        fun newInstance(article: Article): ArticleDescriptionFragment {
            val fragment = ArticleDescriptionFragment()
            val bundle = bundleOf(Pair("ARTICLE", article))
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleOwner = activity as AppCompatActivity
        article = arguments?.get("ARTICLE") as Article

        val transition = TransitionInflater.from(context).inflateTransition(R.transition.shared_element_transition)
        sharedElementEnterTransition = transition
//        sharedElementReturnTransition = transition
        postponeEnterTransition()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_article_description, container, false)
        rootView.apply {
            newsTitle.text = article.title
            news_author.text = "From ${article.author}"
            news_desc.text = article.desc
            news_time.text = UtilFunctions.getRelativeTimeSpanString(article.publishedAt)
            open_article_button.setOnClickListener {
                // TODO: Open Chrome custom tabs or webview
            }
//            val requestOptions = RequestOptions
            val listener = object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }

            }
            Glide.with(context).load(article.urlToImage)
                .listener(listener).dontAnimate().into(rootView.newsThumbnail)
        }
        return rootView
    }
}