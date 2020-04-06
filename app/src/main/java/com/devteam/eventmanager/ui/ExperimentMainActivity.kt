package com.devteam.eventmanager.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.devteam.eventmanager.R
import com.devteam.eventmanager.common.or
import kotlinx.android.synthetic.main.activity_experiment_main.*

class ExperimentMainActivity : AppCompatActivity() {

    // get the section references
    val sectionMyEventsWrapper: FrameLayout by lazy { section_my_events_wrapper }
    val sectionTodaysEventsWrapper: FrameLayout by lazy { section_todays_events_wrapper }
    val sectionUpcomingEventsWrapper: FrameLayout by lazy { section_upcoming_events_wrapper }

    // get the nav controller corresponding to bottom nav sections
    val navControllerMyEvents: NavController by lazy { findNavController(R.id.section_my_events) }
    val navControllerTodaysEvents: NavController by lazy { findNavController(R.id.section_todays_events) }
    val navControllerUpcomingEvents: NavController by lazy { findNavController(R.id.section_upcoming_events) }

    // get the fragment references
    val fragmentMyEvents: Fragment by lazy { section_my_events }
    val fragmentTodayEvents: Fragment by lazy { section_todays_events }
    val fragmentUpcomingEvents: Fragment by lazy { section_upcoming_events }

    // current Nav Controller reference
    var currentNavController: NavController? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_experiment_main)

        currentNavController = navControllerMyEvents

        navigation.setOnNavigationItemSelectedListener {
            Toast.makeText(this, "Item clicked - $it", Toast.LENGTH_SHORT).show()
            var returnValue = false

            when (it.itemId) {
                R.id.menu_my_events -> {
                    setMyEventsNavAsCurrentNav()
                    returnValue = true
                }
                R.id.menu_todays_events -> {
                    setTodaysEventsNavAsCurrentNav()
                    returnValue = true
                }
                R.id.menu_upcoming_events -> {
                    setUpcomingEventsNavAsCurrentNav()
                    returnValue = true
                }
            }
            //onReselected(item.itemId)
            //return@OnNavigationItemSelectedListener returnValue
            returnValue
        }

        sectionMyEventsWrapper.visibility = View.VISIBLE
        sectionTodaysEventsWrapper.visibility = View.INVISIBLE
        sectionUpcomingEventsWrapper.visibility = View.INVISIBLE
    }

    private fun setMyEventsNavAsCurrentNav() {
        currentNavController = navControllerMyEvents

        sectionMyEventsWrapper.visibility = View.VISIBLE
        sectionTodaysEventsWrapper.visibility = View.INVISIBLE
        sectionUpcomingEventsWrapper.visibility = View.INVISIBLE
    }

    private fun setTodaysEventsNavAsCurrentNav() {
        currentNavController = navControllerTodaysEvents

        sectionMyEventsWrapper.visibility = View.INVISIBLE
        sectionTodaysEventsWrapper.visibility = View.VISIBLE
        sectionUpcomingEventsWrapper.visibility = View.INVISIBLE
    }

    private fun setUpcomingEventsNavAsCurrentNav() {
        currentNavController = navControllerUpcomingEvents

        sectionMyEventsWrapper.visibility = View.INVISIBLE
        sectionTodaysEventsWrapper.visibility = View.INVISIBLE
        sectionUpcomingEventsWrapper.visibility = View.VISIBLE
    }


    override fun supportNavigateUpTo(upIntent: Intent) {
        currentNavController?.navigateUp()
    }

    override fun onBackPressed() {
        currentNavController
            ?.let { if (it.popBackStack().not()) finish() }
            .or { finish() }
    }
}
