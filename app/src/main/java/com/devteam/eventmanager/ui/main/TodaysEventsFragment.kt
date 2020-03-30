package com.devteam.eventmanager.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devteam.eventmanager.R

class TodaysEventsFragment : Fragment() {

    companion object {
        fun newInstance() = TodaysEventsFragment()
    }

    private lateinit var viewModel: TodaysEventViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_todays, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TodaysEventViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
