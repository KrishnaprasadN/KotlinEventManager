package com.devteam.eventmanager.ui.addevent

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.devteam.eventmanager.databinding.ActivityAddEventBinding


class AddEventActivity : AppCompatActivity() {

    private val viewModel: AddEventViewModel
            by lazy {
                ViewModelProvider(this).get(AddEventViewModel::class.java)
            }
    private lateinit var mBinding: ActivityAddEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(
            this, com.devteam.eventmanager.R.layout.activity_add_event
        )

        mBinding.viewModel = viewModel

        // Finish the activity when onFinish is set to true
        viewModel.onFinish.observe(this,
            Observer<Boolean> { onEnd ->
                if (onEnd != null && onEnd) {
                    finish()
                }
            })

    }

}
