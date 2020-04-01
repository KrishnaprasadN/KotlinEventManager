package com.devteam.eventmanager.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.devteam.eventmanager.R
import com.devteam.eventmanager.databinding.FragmentTodaysBinding

class TodaysEventsFragment : Fragment() {

    companion object {
        fun newInstance() = TodaysEventsFragment()
    }

    private lateinit var viewModel: TodaysEventViewModel

    private lateinit var binding: FragmentTodaysBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_todays, container, false)
        binding = FragmentTodaysBinding.inflate(inflater, view as ViewGroup, false)
        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.showDetails.setOnClickListener {
            findNavController().navigate(R.id.eventDetailsFragment)
        }
    }

}
