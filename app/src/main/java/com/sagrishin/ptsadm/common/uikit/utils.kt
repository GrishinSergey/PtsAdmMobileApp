package com.sagrishin.ptsadm.common.uikit

import android.content.Context
import android.util.DisplayMetrics
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.util.*

fun toPx(dp: Float, context: Context): Float {
    return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

fun toDp(px: Float, context: Context): Float {
    return px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}
