package com.sagrishin.ptsadm.common

import androidx.annotation.PluralsRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun Fragment.getPluralBy(@PluralsRes res: Int, quantity: Int): String {
    return resources.getQuantityString(res, quantity, quantity)
}

fun AppCompatActivity.getPluralBy(@PluralsRes res: Int, quantity: Int): String {
    return resources.getQuantityString(res, quantity, quantity)
}

