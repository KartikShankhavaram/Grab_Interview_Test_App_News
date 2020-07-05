package com.kartik.grabinterviewtestapp_news.ui.activities

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.kartik.grabinterviewtestapp_news.R
import kotlinx.android.synthetic.main.activity_webview.*


class WebViewActivity : AppCompatActivity() {

    val EXTRA_URL = "extra.url"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        val url = intent.getStringExtra(EXTRA_URL)
        title = url
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        webview.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            loadUrl(url)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        onBackPressed()
        return true
    }
}