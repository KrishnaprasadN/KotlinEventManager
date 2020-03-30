package com.devteam.eventmanager

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.devteam.eventmanager.databinding.MainActivityBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: MainActivityBinding = DataBindingUtil.setContentView(
            this, R.layout.main_activity
        )

        // set toolbar as action bar
        setSupportActionBar(binding.toolbar)

        // get nav host fragment
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return

        // setup navigation controller to appbar mapping
        // this updates the action bar with required title, arrow based on nav destination
        val navController = host.navController
        val appBarConfiguration = setupAppbarConfig()
        setupActionBarWithNavController(navController, appBarConfiguration)

        // setup the bottom nav item listener
        setupBottomNavMenu(binding.bottomNavView, navController)
    }

    private fun setupAppbarConfig(): AppBarConfiguration {
        // Build top level destinations, for these destinations,
        // we don't get up/left arrow in toolbar for these destinations
        return AppBarConfiguration
            .Builder(
                R.id.myEventsFragment,
                R.id.todaysEventsFragment,
                R.id.upcomingEventsFragment
            )
            .build()
    }

    private fun setupBottomNavMenu(bottomNav: BottomNavigationView, navController: NavController) {
        bottomNav?.setOnNavigationItemSelectedListener {
            Toast.makeText(this, "Clicked - $it", Toast.LENGTH_SHORT).show()
            when (it.itemId) {
                R.id.action_my_events -> navController.navigate(R.id.myEventsFragment)
                R.id.action_todays_events -> navController.navigate(R.id.todaysEventsFragment)
                R.id.action_upcoming_events -> navController.navigate(R.id.upcomingEventsFragment)
            }

            true
        }
    }

}
