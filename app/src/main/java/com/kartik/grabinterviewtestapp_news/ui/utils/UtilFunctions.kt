package com.kartik.grabinterviewtestapp_news.ui.utils

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.text.format.DateUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class UtilFunctions {
    companion object {
        fun getRelativeTimeSpanString(dateString: String?): CharSequence {
            var ago: CharSequence = ""
            if (dateString != null) {
                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                sdf.timeZone = TimeZone.getTimeZone("GMT")
                try {
                    val time: Long = sdf.parse(dateString).time
                    val now = System.currentTimeMillis()
                    ago =
                        DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            }
            return ago
        }

        private const val SERVICE_ACTION =
            "android.support.customtabs.action.CustomTabsService"
        private const val CHROME_PACKAGE = "com.android.chrome"

        fun isChromeCustomTabsSupported(context: Context): Boolean {
            val serviceIntent = Intent()
            serviceIntent.setPackage(CHROME_PACKAGE)
            val resolveInfos: List<ResolveInfo> =
                context.packageManager.queryIntentServices(serviceIntent, 0)
            return !(resolveInfos == null || resolveInfos.isEmpty())
        }
    }
}