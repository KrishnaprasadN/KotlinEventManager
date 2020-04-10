package com.devteam.eventmanager.ui.registration

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.*
import androidx.lifecycle.*
import com.devteam.eventmanager.ui.ExperimentMainActivity
import com.devteam.eventmanager.utility.FirebaseStorageUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.OnProgressListener
import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RegisterViewModel : ViewModel()/*, Observable*/ {
    internal var fileUri: Uri? = null
    lateinit var firebaseUser: FirebaseUser
    lateinit var name: String
    var newUser: Boolean = false
    lateinit var uri: Uri

    var registeringUser: ObservableBoolean = ObservableBoolean()

    var inValidNameError: ObservableField<String> = ObservableField<String>("")

    enum class RegistrationStatus {
        SUCCESS,
        FAIL,
        NO_NETWORK,
    }

    val registerExceptionhandler = CoroutineExceptionHandler { _, exception ->
        registeringUser.set(false)
        RegistrationStatus.FAIL
        Log.d("registerException", "${exception.message}")
    }

   fun registerUser() : LiveData<RegistrationStatus>{
        val registrationStatus  = MutableLiveData<RegistrationStatus>()
        viewModelScope.launch(Dispatchers.Main + registerExceptionhandler) {
            val name = object {}.javaClass.enclosingMethod?.name
            val thread = Thread.currentThread().name
            Log.d("register", "********** register registerUser  $thread")
            val updateProifleRegistrationStatus = async { updateProifleUri() }
            val updateProifleDisplayNameRegistrationStatus = async { updateProifleDisplayName() }
            if (updateProifleRegistrationStatus.await() && updateProifleDisplayNameRegistrationStatus.await()) {
                registrationStatus.value = RegistrationStatus.SUCCESS
            } else {
                registrationStatus.value = RegistrationStatus.FAIL
            }
        }
        return registrationStatus
    }


    /**
     * Update user display name to firebase
     */
    internal suspend fun updateProifleDisplayName(): Boolean {
        val thread = Thread.currentThread().name
        Log.d("register", "********** register updateProifleDisplayName  $thread")
        if (!firebaseUser.displayName.equals(name)) {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
            return updateProfile(profileUpdates)
        } else return true
    }

    /**
     * Update user photo uri to firebase
     */
    internal suspend fun updateProifleUri(): Boolean {
        val thread = Thread.currentThread().name
        Log.d("register", "********** register updateProifleUri  $thread")
        return withContext(Dispatchers.IO) {
            fileUri?.let {
                val uri = uploadProfilePic()
                uri?.let {
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setPhotoUri(uri)
                        .build()
                    updateProfile(profileUpdates)
                } ?: false
            } ?: true
        }
    }


    /**
     * upload image to firebase and get download uri
     */
    private suspend fun uploadProfilePic(): Uri? {
        // call withContext(Dispatchers.IO) to create a block that runs on the IO thread pool.
        return withContext<Uri?>(Dispatchers.IO) {
            suspendCoroutine { cont ->
                val thread = Thread.currentThread().name
                Log.d("register", "********** register uploadProfilePic  $thread")
                FirebaseStorageUtils().uploadProfilePhoto(fileUri, OnFailureListener {
                    cont.resume(null)
                }, OnSuccessListener {
                    it.storage.getDownloadUrl().addOnSuccessListener(OnSuccessListener<Uri> { uri ->
                        cont.resume(uri)
                    })

                }, OnProgressListener {
                    val progress = (100.0 * it.bytesTransferred / it.totalByteCount)
                    //bind.progressBar.message.text = "Uploaded "+progress+"%"
                    //bind.progressBar.progress = progress.toInt()
                })
            }
        }
    }

    /**
     * update user profile to firebase
     */
    suspend fun updateProfile(
        profileUpdates: UserProfileChangeRequest
    ): Boolean {
        return withContext(Dispatchers.IO) {
            suspendCoroutine<Boolean> { cont ->
                run {
                    val thread = Thread.currentThread().name
                    Log.d("register", "********** register updateProfile $thread")
                    firebaseUser.updateProfile(profileUpdates)
                        .addOnCompleteListener(object : OnCompleteListener<Void> {
                            override fun onComplete(task: Task<Void>) {
                                if (task.isSuccessful) {
                                    cont.resume(true)
                                } else {
                                    cont.resume(false)
                                }
                            }

                        })
                }
            }
        }

    }
}

/*
    private val callbacks = PropertyChangeRegistry()
    var registeringUser: Boolean = false
        set(value) {
            field = value
            callbacks.notifyCallbacks(this, 0, null)
        }*/


/*  var inValidNameError: String = ""
    set(value) {
     field = value
     callbacks.notifyCallbacks(this, 0, null)
 }*/


/*override fun addOnPropertyChangedCallback(
    callback: Observable.OnPropertyChangedCallback
) {
    callbacks.add(callback)
}

override fun removeOnPropertyChangedCallback(
    callback: Observable.OnPropertyChangedCallback
) {
    callbacks.remove(callback)
}*/
