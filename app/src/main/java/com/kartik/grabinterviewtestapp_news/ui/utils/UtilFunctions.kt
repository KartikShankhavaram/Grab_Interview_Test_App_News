package com.kartik.grabinterviewtestapp_news.ui.utils

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
    }
}