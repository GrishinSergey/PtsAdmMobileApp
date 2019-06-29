package com.sagrishin.ptsadm.common.navigation

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

fun <R> FragmentManager.use(now: Boolean = false, fm: FragmentTransaction.() -> R): R {
    val transaction = beginTransaction()
    val res = transaction.fm()

    if (now) {
        transaction.commitNow()
    } else {
        transaction.commit()
    }

    return res
}