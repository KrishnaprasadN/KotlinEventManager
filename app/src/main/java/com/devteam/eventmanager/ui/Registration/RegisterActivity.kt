package com.devteam.eventmanager.ui.Registration

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.devteam.eventmanager.MainActivity
import com.devteam.eventmanager.R
import com.devteam.eventmanager.databinding.ActivityRegisterBinding
import com.devteam.eventmanager.ui.ExperimentMainActivity
import com.devteam.eventmanager.utility.FirebaseStorageUtils
import com.devteam.eventmanager.utility.SharedPreferences
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.api.Experimental
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.OnProgressListener
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class RegisterActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var bind: ActivityRegisterBinding
    private var fileUri: Uri? = null
    lateinit var firebaseUser: FirebaseUser


    //Scopes in Kotlin Coroutines are very useful because we need to cancel the background task as soon as the activity is destroyed
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job   // Dispatchers.Main + job + handler  <- one more way to handle Exception in activity scope
    private lateinit var job: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(bind.root)
        setSupportActionBar(toolbar)
        setTitle(R.string.profile)

        init()

    }

    override fun onDestroy() {
        job.cancel() // cancel the Job
        super.onDestroy()
    }

    private fun init() {
        job = Job()

        val auth = FirebaseAuth.getInstance()
        firebaseUser = auth.currentUser!!
        bind.name = auth.currentUser?.displayName
        bind.uri = auth.currentUser?.photoUrl

        bind.newUser = TextUtils.isEmpty(bind.name)

    }

    fun skip(view: View) {
        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * Image picker
     */
    fun uploadImage(view: View) {
        ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                        1080,
                        1080
                )    //Final image resolution will be less than 1080 x 1080(Optional)
                .start { resultCode, data ->
                    if (resultCode == Activity.RESULT_OK) {
                        //Image Uri will not be null for RESULT_OK
                        fileUri = data?.data
                        bind.profilePic.setImageURI(fileUri)

                        //You can get File object from intent
                        // val file: File? = ImagePicker.getFile(data)

                        //You can also get File Path from intent
                        //  val filePath:String? = ImagePicker.getFilePath(data)

                    } else if (resultCode == ImagePicker.RESULT_ERROR) {
                        Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                    }
                }

    }

    //Exception Handling in Kotlin Coroutines
    val handler = CoroutineExceptionHandler { _, exception ->
        bind.registeringUser = false
        Toast.makeText(
                this@RegisterActivity,
                "register fail",
                Toast.LENGTH_LONG
        ).show()
    }

    /**
     * register user profile image and display name
     */
    fun register(view: View) {
        bind.name?.let {
            launch(Dispatchers.Main + handler) {
                bind.registeringUser = true
                val updateProifleStatus = async { updateProifleUri() }
                val updateProifleDisplayNameStatus = async { updateProifleDisplayName() }
                if (updateProifleStatus.await() && updateProifleDisplayNameStatus.await()) {
                       // goToHomePage()
                        Toast.makeText(
                                this@RegisterActivity,
                                "register success",
                                Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                                this@RegisterActivity,
                                "register fail",
                                Toast.LENGTH_LONG
                        ).show()
                    }
                    bind.registeringUser = false
            }

        } ?: bind.userName.setError(getString(R.string.enter_vaild_phone));


    }

    /**
     * Take user to home screen
     */
    private fun goToHomePage() {
        SharedPreferences().loogedIn(baseContext)
        val intent =
                Intent(this@RegisterActivity, ExperimentMainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }


    /**
     * Update user display name to firebase
     */
    private suspend fun updateProifleDisplayName(): Boolean {
        if (!firebaseUser.displayName.equals(bind.name)) {
            val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(bind.name)
                    .build()
            return updateProfile(profileUpdates)
        } else return true
    }

    /**
     * Update user photo uri to firebase
     */
    private suspend fun updateProifleUri(): Boolean {
        return withContext(Dispatchers.IO) {
            fileUri?.let {
                val uri = uploadProfilePic()
                uri?.let {
                    val profileUpdates = UserProfileChangeRequest.Builder()
                            .setPhotoUri(uri)
                            .build()
                     updateProfile(profileUpdates)
                } ?:  false
            } ?:  true
        }
    }

    /**
     * upload image to firebase and get download uri
     */
    private suspend fun uploadProfilePic(): Uri? {
        // call withContext(Dispatchers.IO) to create a block that runs on the IO thread pool.
        return withContext<Uri?>(Dispatchers.IO){
             suspendCoroutine { cont ->
                FirebaseStorageUtils().uploadProfilePhoto(fileUri, OnFailureListener {
                    cont.resume(null)
                }, OnSuccessListener {
                    it.storage.getDownloadUrl().addOnSuccessListener(OnSuccessListener<Uri> { uri ->
                        cont.resume(uri)
                    })

                }, OnProgressListener {
                    val progress = (100.0 * it.bytesTransferred / it.totalByteCount)
                    //bind.progressBar.message.text = "Uploaded "+progress+"%"
                    bind.progressBar.progress = progress.toInt()
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
        return suspendCoroutine { cont ->
            run {
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


