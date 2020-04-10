package com.devteam.eventmanager.ui.registration

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.devteam.eventmanager.R
import com.devteam.eventmanager.databinding.ActivityRegisterBinding
import com.devteam.eventmanager.ui.ExperimentMainActivity
import com.devteam.eventmanager.utility.SharedPreferences
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*


class RegisterActivity : AppCompatActivity()/*, CoroutineScope*/ {
    private lateinit var bind: ActivityRegisterBinding
    lateinit var viewModel: RegisterViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityRegisterBinding.inflate(layoutInflater)
        bind.setLifecycleOwner(this)

        setContentView(bind.root)
        setSupportActionBar(toolbar)
        setTitle(R.string.profile)

        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        init()

    }


    private fun init() {
        //job = Job()
        val auth = FirebaseAuth.getInstance()
        viewModel.firebaseUser = auth.currentUser!!
        viewModel.name = auth.currentUser?.displayName!!
        viewModel.uri = auth.currentUser?.photoUrl!!
        viewModel.newUser = TextUtils.isEmpty(viewModel.name)
        bind.registerViewModel = viewModel
    }

    fun skip(view: View) {
        goToHomePage()
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
                    viewModel.fileUri = data?.data
                    bind.profilePic.setImageURI(viewModel.fileUri)

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


    private fun goToHomePage() {
        SharedPreferences().loogedIn(baseContext)
        val intent =
            Intent(this@RegisterActivity, ExperimentMainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    fun register(view: View) {
        with(viewModel) {
            if (registeringUser.get())
                return
            if (name.length < 3) {
                inValidNameError.set(view.context.getString(R.string.enter_vaild_phone))
                inValidNameError.notifyChange()
            } else {
                registeringUser.set(true)
                viewModel.registerUser().observe(this@RegisterActivity, Observer {
                    Log.d("SingleLiveEvent","****SingleLiveEvent Observer")
                    registeringUser.set(false)
                    handleStatus(it)

                    viewModel.registerUser().removeObservers(this@RegisterActivity)
                })
            }

        }
    }

    private fun handleStatus(status: RegisterViewModel.RegistrationStatus?) {
            when (status) {
                RegisterViewModel.RegistrationStatus.FAIL -> Toast.makeText(
                    baseContext,
                    "register fail",
                    Toast.LENGTH_LONG
                ).show()
                RegisterViewModel.RegistrationStatus.SUCCESS -> {
                    Toast.makeText(
                        baseContext,
                        "register success",
                        Toast.LENGTH_LONG
                    ).show()
                    goToHomePage()
                }
                else -> Toast.makeText(baseContext, "Something went wrong, please try again!", Toast.LENGTH_SHORT).show()
            }
    }
}


//Scopes in Kotlin Coroutines are very useful because we need to cancel the background task as soon as the activity is destroyed
/*override val coroutineContext: CoroutineContext
    get() = Dispatchers.Main + job   // Dispatchers.Main + job + handler  <- one more way to handle Exception in activity scope
private lateinit var job: Job*/


/**
 *  *   //Exception Handling in Kotlin Coroutines
 */
/*
val handler = CoroutineExceptionHandler { _, exception ->
    viewModel.registeringUser = false
    Toast.makeText(
        this@RegisterActivity,
        "register fail",
        Toast.LENGTH_LONG
    ).show()
}
*/

/***
 * suspending functions cannot be called directly from regular functions. For that, we need to use
startAndForget {
//call your suspend function here
}
 */
/* fun startAndForget(suspendingFunction: suspend () -> Unit) {
     suspendingFunction.startCoroutine(object : Continuation<Unit> {

         override val context: CoroutineContext
             get() = EmptyCoroutineContext

         override fun resumeWith(result: Result<Unit>) {
             TODO("Not yet implemented")
         }
     })
 }*/


/**
 * /*register user profile image and display name*/
 */
/*
fun register(view: View) {

         if(viewModel.registeringUser)
             return

            viewModel.registeringUser = true
             if(viewModel.name.length < 3){
                  bind.userName.setError(getString(R.string.enter_vaild_phone));
             }else {
                 launch(Dispatchers.Main + handler) {



                     viewModel.registeringUser = true

                     val updateProifleStatus = async { viewModel.updateProifleUri() }
                     val updateProifleDisplayNameStatus = async { viewModel.updateProifleDisplayName() }
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
                     viewModel.registeringUser = false
                     bind?.invalidateAll()
                 }
             }
    }*/
