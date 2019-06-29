package com.sagrishin.ptsadm.common.uikit.alertdialog

import android.content.Context
import android.content.DialogInterface
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sagrishin.ptsadm.R

fun Fragment.alert(l: AlertHelper.() -> Unit) {
    return AlertHelper().apply { l() }.create(context!!).show()
}


fun AppCompatActivity.alert(l: AlertHelper.() -> Unit) {
    return AlertHelper().apply { l() }.create(this).show()
}


class AlertHelper {

    @StringRes var titleId: Int? = null
    @StringRes var messageId: Int? = null
    var title = ""
    var message = ""

    var positiveButtonColor = R.color.colorPrimaryDark
    var negativeButtonColor = R.color.colorGray
    var positiveButtonId: Pair<Int, ((DialogInterface) -> Unit)>? = null
    var negativeButtonId: Pair<Int, ((DialogInterface) -> Unit)>? = null

    fun create(context: Context): AlertDialog {
        return AlertDialog.Builder(context).run {
            setTitle(titleId?.let { context.resources.getString(it) } ?: let { title })
            setMessage(messageId?.let { context.resources.getString(it) } ?: let { message })
            positiveButtonId?.let { setPositiveButton(context.resources.getString(it.first)) { d, _ -> it.second(d) } }
            negativeButtonId?.let { setNegativeButton(context.resources.getString(it.first)) { d, _ -> it.second(d) } }
            show().apply {
                getButton(DialogInterface.BUTTON_POSITIVE)
                    .setTextColor(context.resources.getColor(positiveButtonColor, context.theme))
                getButton(DialogInterface.BUTTON_NEGATIVE)
                    .setTextColor(context.resources.getColor(negativeButtonColor, context.theme))
            }
        }
    }

}
