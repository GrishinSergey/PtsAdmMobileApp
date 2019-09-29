package com.sagrishin.ptsadm.common

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

fun Fragment.show(l: () -> DialogFragment) {
    l().let { it.show(childFragmentManager, it.javaClass.name) }
}