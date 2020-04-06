package com.devteam.eventmanager.ui.myevents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.devteam.eventmanager.R
import com.devteam.eventmanager.databinding.FragmentMyEventsBinding

class MyEventsFragment : Fragment() {

    lateinit var binding: FragmentMyEventsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_events, container, false)
        binding = FragmentMyEventsBinding.inflate(inflater, view as ViewGroup, false)
        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.showDetails.setOnClickListener {
            findNavController().navigate(R.id.action_myEventsFragment2_to_eventDetailsFragment2)
        }

        binding.addEvent.setOnClickListener {
            findNavController().navigate(R.id.action_myEventsFragment2_to_addEventActivity)
        }
    }
}
