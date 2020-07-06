package com.kartik.grabinterviewtestapp_news.ui.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.kartik.grabinterviewtestapp_news.R
import kotlinx.android.synthetic.main.activity_webview.*
import kotlinx.android.synthetic.main.webview_toolbar_layout.view.*


class WebViewActivity : AppCompatActivity() {

    val EXTRA_URL = "extra.url"
    val EXTRA_TITLE = "extra.title"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        val url = intent.getStringExtra(EXTRA_URL)
        setSupportActionBar(webview_toolbar)
        setToolbarAttrs(LayoutInflater.from(this), intent.getStringExtra(EXTRA_TITLE))
        webview.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            loadUrl(url)
        }
    }

    fun setToolbarAttrs(inflater: LayoutInflater, title: String?) {
        val customToolbar = inflater.inflate(R.layout.webview_toolbar_layout, null)
        val actionBar = supportActionBar
        actionBar?.apply {
            customView = customToolbar
            setDisplayShowCustomEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
        customToolbar.webview_toolbar_close_button.setOnClickListener {
            onBackPressed()
            finish()
        }
        customToolbar.webview_toolbar_textview.apply {
            text = title
            isSelected = true
        }
    }
}