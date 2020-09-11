package com.rhodeon.weathersage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)  // display the main activity
        tempDisplaySettingManager = TempDisplaySettingManager(this)

        navController = findNavController(R.id.nav_host_fragment)
        val navGraph = navController.navInflater.inflate(R.navigation.main_nav)

        // Set up home fragment according to the existence of a saved location
        if (!isLocationEmpty(this)) navGraph.startDestination = R.id.currentForecastFragment    // Start from current weather fragment if there is an existing location
        else navGraph.startDestination = R.id.locationEntryFragment                                      // Start from location entry fragment if there is no saved location

        navController.graph = navGraph

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.locationEntryFragment, R.id.currentForecastFragment, R.id.weeklyForecastFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)

        findViewById<BottomNavigationView>(R.id.bottomNavigationView).setupWithNavController(navController)     // setup bottom nav bar
    }

    // region: Handle Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Populate menu view with settings_menu items
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle when a menu item is selected with the use of its ID
        // Param: item - a clicked or selected menu item
        return when (item.itemId) {
            R.id.units_option -> {
                showChangeUnitDialog(this, tempDisplaySettingManager)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    // endregion: Handle Menu
}
