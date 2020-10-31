package com.rhodeon.weathersage.utils

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.rhodeon.weathersage.R

/**
 * Created by Ogheneruona Onobrakpeya on 10/31/20.
 */

fun Fragment.navigateSafe(direction: NavDirections) {
    val navController = findNavController()
    val destinationIdInNavController = navController.currentDestination?.id

    // Get currently saved id
    val initialFragmentDestinationId = view?.getTag(R.id.tag_navigation_destination_id) ?: destinationIdInNavController

    // Check that the navigation graph is still in 'this' fragment, if so then navigate:
    if (destinationIdInNavController == initialFragmentDestinationId) {
        view?.setTag(R.id.tag_navigation_destination_id, initialFragmentDestinationId)
        navController.navigate(direction)
    }
}