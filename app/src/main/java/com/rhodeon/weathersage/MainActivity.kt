package com.rhodeon.weathersage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.rhodeon.weathersage.databinding.ActivityMainBinding
import com.rhodeon.weathersage.utils.isLocationEmpty

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)  // display the main activity

        navController = findNavController(R.id.nav_host_fragment)
        val navGraph = navController.navInflater.inflate(R.navigation.main_nav)

        // Set up home fragment according to the existence of a saved location
        if (!isLocationEmpty(this)) navGraph.startDestination = R.id.currentForecastFragment    // Start from current weather fragment if there is an existing location
        else navGraph.startDestination = R.id.locationEntryFragment                                      // Start from location entry fragment if there is no saved location

        navController.graph = navGraph
        binding.bottomNavigationView.setupWithNavController(navController)     // setup bottom nav bar
    }
}