package com.kartik.grabinterviewtestapp_news.ui.fragments

import android.content.Context
import android.content.Intent
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsCallback
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.kartik.grabinterviewtestapp_news.R
import com.kartik.grabinterviewtestapp_news.data.database.entities.Article
import com.kartik.grabinterviewtestapp_news.ui.activities.MainActivity
import com.kartik.grabinterviewtestapp_news.ui.activities.WebViewActivity
import com.kartik.grabinterviewtestapp_news.ui.utils.UtilFunctions
import com.kartik.grabinterviewtestapp_news.viewmodel.ArticleViewModel
import kotlinx.android.synthetic.main.article_desc_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_article_description.view.*

class ArticleDescriptionFragment() : Fragment() {
    private lateinit var lifecycleOwner: LifecycleOwner
    private val articleViewModel by activityViewModels<ArticleViewModel>()
    private val mainActivity: MainActivity? = activity as MainActivity?
    private lateinit var article: Article
    val CUSTOM_TAB_PACKAGE_NAME = "com.android.chrome"
    private val TAG = "ArticleDescriptionFragm"

    companion object {
        fun newInstance(article: Article): ArticleDescriptionFragment {
            val fragment =
                ArticleDescriptionFragment()
            val bundle = bundleOf(Pair("ARTICLE", article))
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleOwner = activity as AppCompatActivity
        article = arguments?.get("ARTICLE") as Article

        val transition = TransitionInflater.from(context)
            .inflateTransition(R.transition.shared_element_transition)
        sharedElementEnterTransition = transition
        postponeEnterTransition()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_article_description, container, false)
        articleViewModel.getNetworkStatus().observe(lifecycleOwner, Observer {
            rootView.open_article_button.isEnabled = it
            Log.d(TAG, "onCreateView: network: $it")
        })
        rootView.apply {
            newsTitle.text = with(article.title) {
                val l = this?.lastIndexOf("-")
                val s = this?.substring(startIndex = 0, endIndex = l!!)
                s?.trim()
            }
            if (article.author != null && !article.author.equals("null"))
                news_author.text = article.author
            else
                news_author.visibility = View.GONE
            if(article.desc == null) {
                news_desc.text = "No description."
                news_desc.setTypeface(news_desc.typeface, Typeface.ITALIC)
            }else {
                news_desc.text = article.desc
            }
            news_time.text = UtilFunctions.getRelativeTimeSpanString(article.publishedAt)
            open_article_button.isEnabled = articleViewModel.getNetworkStatus().value ?: false
            Log.e(TAG, "onCreateView: networkstatus: ${articleViewModel.getNetworkStatus().value}")
            open_article_button.setOnClickListener {
                if (UtilFunctions.isChromeCustomTabsSupported(context)) {
                    val customTabsIntent = buildCustomTabs(context)
                    customTabsIntent.intent.setPackage(CUSTOM_TAB_PACKAGE_NAME)
                    customTabsIntent.launchUrl(context, Uri.parse(article.url))
                } else {
                    val intent = Intent(context, WebViewActivity::class.java)
                    intent.putExtra("extra.url", article.url)
                    startActivity(intent)
                }
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
                    setToolbarAttrs(inflater)
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
                    setToolbarAttrs(inflater)
                    return false
                }

            }
            var glide = Glide.with(context).load(article.urlToImage).placeholder(R.drawable.placeholder).error(R.drawable.ic_broken_image)
                .listener(listener).dontAnimate()
            if(resources.configuration.orientation == ORIENTATION_LANDSCAPE)
                glide = glide.centerCrop()
            glide.into(rootView.newsThumbnail)
        }
        return rootView
    }

    override fun onStart() {
        super.onStart()
        mainActivity?.customTabsClient?.newSession(CustomTabsCallback())
            ?.mayLaunchUrl(Uri.parse(article.url), null, null)
    }

    fun buildCustomTabs(context: Context): CustomTabsIntent = CustomTabsIntent.Builder().apply {
        addDefaultShareMenuItem()
        setShowTitle(true)
        setToolbarColor(
            ContextCompat.getColor(
                context,
                R.color.colorPrimary
            )
        )
    }.build()

    fun setToolbarAttrs(inflater: LayoutInflater) {
        val customToolbar = inflater.inflate(R.layout.article_desc_toolbar, null)
        val actionBar = (activity as MainActivity).supportActionBar
        actionBar?.apply {
            customView = customToolbar
            setDisplayShowCustomEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
        customToolbar.toolbar_back_button.setOnClickListener {
            activity?.onBackPressed()
        }
        customToolbar.toolbar_textview.text = article.sourceName
    }
}