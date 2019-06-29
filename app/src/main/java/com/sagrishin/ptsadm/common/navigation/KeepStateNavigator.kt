package com.sagrishin.ptsadm.common.navigation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator

@Navigator.Name("KeepStateFragment")
class KeepStateNavigator(
    private val context: Context,
    private val manager: FragmentManager,
    private val containerId: Int
) : FragmentNavigator(context, manager, containerId) {

    override fun navigate(
        destination: Destination,
        args: Bundle?,
        options: NavOptions?,
        extras: Navigator.Extras?
    ): NavDestination? {
        return manager.use(now = true) {
            val tag = destination.id.toString()
            var initialNavigate = false
            var fragment = manager.findFragmentByTag(tag)

            manager.primaryNavigationFragment?.let {
                detach(it)
            } ?: let {
                initialNavigate = true
            }

            if (fragment == null) {
                fragment = manager.fragmentFactory.instantiate(context.classLoader, destination.className)
                add(containerId, fragment, tag)
            } else {
                attach(fragment)
            }

            setPrimaryNavigationFragment(fragment)
            setReorderingAllowed(true)

            if (initialNavigate) {
                destination
            } else {
                null
            }
        }
    }

}
