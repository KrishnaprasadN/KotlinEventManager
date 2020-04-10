package com.devteam.eventmanager.common

import android.net.Uri
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.EditText
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

@BindingAdapter("error_message")
fun errorMessage(editText: EditText, errorMessage: String) {
    if (TextUtils.isEmpty(errorMessage)) return
    editText.setError(errorMessage);
}
