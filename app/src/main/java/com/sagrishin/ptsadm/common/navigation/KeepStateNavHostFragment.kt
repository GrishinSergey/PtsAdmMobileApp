package com.sagrishin.ptsadm.common.navigation

import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigator.Destination
import androidx.navigation.fragment.NavHostFragment

class KeepStateNavHostFragment: NavHostFragment() {

    override fun createFragmentNavigator(): Navigator<out Destination> {
        return KeepStateNavigator(requireContext(), childFragmentManager, id)
    }

}
