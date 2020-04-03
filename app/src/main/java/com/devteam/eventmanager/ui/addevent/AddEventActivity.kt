package com.devteam.eventmanager.ui.addevent

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.devteam.eventmanager.R
import com.devteam.eventmanager.databinding.ActivityAddEventBinding

class AddEventActivity : AppCompatActivity() {

    private val viewModel: AddEventViewModel
            by lazy {
                ViewModelProvider(this).get(AddEventViewModel::class.java)
            }
    private lateinit var binding: ActivityAddEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_add_event
        )

        binding.viewModel = viewModel
        init()
    }

    private fun init() {

    }
}
