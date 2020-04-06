package com.devteam.eventmanager.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.TextUtilsCompat
import com.devteam.eventmanager.MainActivity
import com.devteam.eventmanager.R
import com.devteam.eventmanager.databinding.ActivityLoginBinding
import com.devteam.eventmanager.ui.Registration.RegisterActivity
import com.devteam.eventmanager.utility.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    lateinit var bind: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(bind.root)
        setSupportActionBar(toolbar)
        setTitle(R.string.login)
        init()
    }

    private fun init() {
        var mAuth = FirebaseAuth.getInstance()
        mAuth.currentUser?.let {
            when (SharedPreferences().isLoogedIn(baseContext)) {
                true -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                false -> {
                    val intent = Intent(this, RegisterActivity::class.java)
                    startActivity(intent)

                }
            }
            finish()
        }
    }

    fun login(view: View) {
        with(bind) {
            loginPhoneNumber?.let {
                if (it.length < 10) {
                    loginPhoneNumb.setError(getString(R.string.enter_vaild_phone));
                    loginPhoneNumb.requestFocus();
                    return;
                }
                val intent = Intent(this@LoginActivity, VerifyPhoneActivity::class.java)
                intent.putExtra("mobile", it)
                startActivity(intent)
            } ?: loginPhoneNumb.setError(getString(R.string.enter_vaild_phone));
        }
    }
}