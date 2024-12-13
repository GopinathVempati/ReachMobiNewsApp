package com.reachmobi.news.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

object Utils {
    fun openArticleInBrowser(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }
}
