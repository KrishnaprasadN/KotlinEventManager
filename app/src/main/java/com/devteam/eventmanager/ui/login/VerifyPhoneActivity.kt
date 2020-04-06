package com.devteam.eventmanager.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.devteam.eventmanager.MainActivity
import com.devteam.eventmanager.R
import com.devteam.eventmanager.databinding.ActivityVerifyPhoneBinding
import com.devteam.eventmanager.ui.Registration.RegisterActivity
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import kotlinx.android.synthetic.main.activity_login.*
import java.util.concurrent.TimeUnit


class VerifyPhoneActivity : AppCompatActivity() {
    lateinit var bind: ActivityVerifyPhoneBinding

    lateinit var mAuth: FirebaseAuth

    lateinit var phoneNumber: String
    lateinit var mVerificationId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityVerifyPhoneBinding.inflate(layoutInflater)
        setContentView(bind.root)
        setSupportActionBar(toolbar)
        setTitle(R.string.verify)

        init()


    }

    private fun init() {
        bind.lifecycleOwner = this
        phoneNumber = intent.getStringExtra("mobile")
        mAuth = FirebaseAuth.getInstance();
        // To apply the default app language instead of explicitly setting it.
        mAuth.setLanguageCode("fr")
        bind.codeSentToPhone = false

        sendVerificationCode(phoneNumber)
    }

    fun verify(view: View) {
        with(bind) {
            phoneVerficationCode?.let {
                val code: String = it.trim()
                when (code.length < 6) {
                    true -> {
                        verficationCode.setError("Enter valid code")
                        verficationCode.requestFocus()
                    }
                    false ->verifyVerificationCode(code)

                }
                //verifying the code entered manually

            } ?: verficationCode.setError("Enter valid code")
        }
    }

    //the method is sending verification code
    //the country id is concatenated
    //you can take the country id as user input as well
    private fun sendVerificationCode(mobile: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91".plus(mobile),
                30,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks
        )
    }

    //the callback to detect the verification status
    private val mCallbacks: OnVerificationStateChangedCallbacks =
            object : OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {

                    //Getting the code sent by SMS
                    val code = phoneAuthCredential.smsCode

                    //sometime the code is not detected automatically
                    //in this case the code will be null
                    //so user has to manually enter the code
                    if (code != null) {
                        bind.verficationCode.setText(code)
                        //verifying the code
                        verifyVerificationCode(code)
                    }
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    bind.info.text =  e.message
                    Toast.makeText(this@VerifyPhoneActivity, e.message, Toast.LENGTH_LONG).show()
                }

                override fun onCodeSent(
                        s: String,
                        forceResendingToken: ForceResendingToken
                ) {
                    super.onCodeSent(s, forceResendingToken)
                    bind.codeSentToPhone = true
                    //storing the verification id that is sent to the user
                    mVerificationId = s
                }
            }

    private fun verifyVerificationCode(code: String) {
        bind.verifyingUser = true
        //creating the credential
        val credential = PhoneAuthProvider.getCredential(mVerificationId, code)

        //signing the user
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(this
        ) { task ->
            bind.verifyingUser = false
            if (task.isSuccessful) {
                //verification successful we will start the profile activity
                mAuth.currentUser?.let {
                    val intent = Intent(this, RegisterActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                finish();
            } else {

                //verification unsuccessful.. display an error message
                var message: String = "Somthing is wrong..."
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    message = "Invalid code entered..."
                }
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }

    }
}