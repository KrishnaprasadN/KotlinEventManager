package com.devteam.eventmanager.common

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.devteam.eventmanager.R

fun ImageView.loadImageByUrl(uri: Uri?) {
    uri?.let {
        Glide.with(this).load(uri).placeholder(R.drawable.user_default).into(this)
    }
}