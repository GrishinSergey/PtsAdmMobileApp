package com.sagrishin.ptsadm.common.uikit.alertdialog

import android.content.Context
import android.content.DialogInterface
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.sagrishin.ptsadm.R

fun Fragment.alert(l: AlertHelper.() -> Unit) {
    return AlertHelper().apply { l() }.create(context!!).show()
}


fun AppCompatActivity.alert(l: AlertHelper.() -> Unit) {
    return AlertHelper().apply { l() }.create(this).show()
}


class AlertHelper {

    @StringRes
    var titleId: Int? = null
    @StringRes
    var messageId: Int? = null
    var title: CharSequence = ""
    var message: CharSequence = ""
    var view: View? = null

    var isCancelable = false

    var positiveButtonColor = R.color.colorPrimaryDark
    var negativeButtonColor = R.color.colorGray
    var neutralButtonColor = R.color.colorGray
    var neutralButton: Pair<Int, ((DialogInterface) -> Unit)>? = null
    var positiveButton: Pair<Int, ((DialogInterface) -> Unit)>? = null
    var negativeButton: Pair<Int, ((DialogInterface) -> Unit)>? = null

    fun create(context: Context): AlertDialog {
        return AlertDialog.Builder(context).run {
            setTitle(titleId?.let { context.resources.getString(it) } ?: title)

            view?.let { setView(view) } ?: setMessage(messageId?.let { context.resources.getString(it) } ?: message)

            setCancelable(isCancelable)

            positiveButton?.let { setPositiveButton(context.resources.getString(it.first)) { d, _ -> it.second(d) } }
            negativeButton?.let { setNegativeButton(context.resources.getString(it.first)) { d, _ -> it.second(d) } }
            neutralButton?.let { setNeutralButton(context.resources.getString(it.first)) { d, _ -> it.second(d) } }

            show().apply {
                getButton(DialogInterface.BUTTON_POSITIVE)
                    .setTextColor(ContextCompat.getColor(context, positiveButtonColor))
                getButton(DialogInterface.BUTTON_NEGATIVE)
                    .setTextColor(ContextCompat.getColor(context, negativeButtonColor))
                getButton(DialogInterface.BUTTON_NEUTRAL)
                    .setTextColor(ContextCompat.getColor(context, neutralButtonColor))
            }
        }
    }

}
