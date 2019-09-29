package com.sagrishin.ptsadm.common

import androidx.activity.OnBackPressedCallback
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.NavDirections

abstract class BaseNavHostActivity(@LayoutRes layoutResId: Int) : AppCompatActivity(layoutResId) {

    abstract fun getController(): NavController

    fun navigateTo(@IdRes destination: Int) {
        getController().navigate(destination)
    }

    fun navigateTo(navDirections: NavDirections) {
        getController().navigate(navDirections)
    }

    fun back() {
        getController().popBackStack()
    }

    fun close() {
        finish()
    }

}


fun FragmentActivity.addOnBackPressedCallback(fragment: Fragment, callback: () -> Unit) {
    onBackPressedDispatcher.addCallback(fragment, object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            callback()
        }
    })
}
