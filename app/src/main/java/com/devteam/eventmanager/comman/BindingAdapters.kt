package com.devteam.eventmanager.comman

import android.net.Uri
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter

@BindingAdapter("loadImage")
fun loadImage(imagView: ImageView, uri: Uri?) {
    imagView.loadImageByUrl(uri)
}

@BindingAdapter("visibility")
fun visibility(progressBar: ProgressBar, visible: Boolean) {
    progressBar.visibility = if(visible) View.VISIBLE else View.GONE
}