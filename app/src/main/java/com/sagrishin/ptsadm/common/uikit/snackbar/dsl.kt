package com.sagrishin.ptsadm.common.uikit.snackbar

import android.content.Context
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar

fun FragmentActivity.snackbar(l: SnackbarHelper.() -> Unit) {
    SnackbarHelper().apply(l).build(this).show()
}


fun Fragment.snackbar(l: SnackbarHelper.() -> Unit) {
    activity!!.snackbar(l)
}


class SnackbarHelper {

    lateinit var view: View
    var message: String? = null
    @StringRes
    var messageId: Int? = null
    var duration: Int = Snackbar.LENGTH_SHORT
    var actionResId: (Pair<Int, (View) -> Unit>)? = null
    var actionTextTitle: (Pair<String, (View) -> Unit>)? = null

    fun build(context: Context): Snackbar {
        val message = this.message?.let { it } ?: messageId?.let { context.getString(it) }
        if (message == null) throw IllegalArgumentException("message or messageId must not be null")
        return Snackbar.make(view, message, duration).apply {
            actionTextTitle?.let {
                setAction(it.first, it.second)
            } ?: actionResId?.let {
                setAction(it.first, it.second)
            }
        }
    }

}
