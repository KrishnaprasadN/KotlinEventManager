package com.devteam.eventmanager.utility

import android.net.Uri
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.UploadTask


class FirebaseStorageUtils {
    fun uploadProfilePhoto(
        uri: Uri?,
        failureListener: OnFailureListener,
        successListener: OnSuccessListener<in UploadTask.TaskSnapshot>,
        progressListener: OnProgressListener<in UploadTask.TaskSnapshot>
    ) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference

        uri?.let {
            val riversRef = storageRef.child("userProfile/${uri.lastPathSegment}")
            val uploadTask = riversRef.putFile(uri)
            // Register observers to listen for when the download is done or if it fails

            uploadTask.addOnFailureListener(failureListener).addOnSuccessListener(successListener)
                .addOnProgressListener(progressListener)
        }
    }
}
