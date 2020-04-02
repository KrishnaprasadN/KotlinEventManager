package com.devteam.eventmanager.ui.addevent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.devteam.eventmanager.R
import com.devteam.eventmanager.databinding.ActivityAddEventBinding
import com.devteam.eventmanager.databinding.MainActivityBinding

class AddEventActivity : AppCompatActivity() {


    private lateinit var binding: ActivityAddEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_add_event
        )

        init()
    }

    private fun init() {

    }
}
