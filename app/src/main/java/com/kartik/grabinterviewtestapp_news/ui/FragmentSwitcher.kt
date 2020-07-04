package com.kartik.grabinterviewtestapp_news.ui

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.kartik.grabinterviewtestapp_news.data.database.entities.Article

interface FragmentSwitcher {
    fun switchFragment(selectedArticle: Article, currentFragment: Fragment, itemView: View)
}