package com.konztic.chatzillo.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.konztic.chatzillo.R
import com.konztic.chatzillo.utilities.goToActivity
import com.konztic.chatzillo.utilities.toast
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity : AppCompatActivity() {
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        btn_go_login.setOnClickListener {
            goToActivity<LoginActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        btn_sign_up.setOnClickListener {
            val email = et_email.text.toString()
            val password = et_password.text.toString()

            if (isValidEmailAndPassword(email, password)) {
                signUpByEmail(email, password)
            } else {
                toast("Please fill all data and confirm that your password is correct")
            }
        }
    }

    private fun signUpByEmail(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                toast("An email has been sent to you. Please, confirm before sign in")
                goToActivity<LoginActivity> {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            } else {
                toast("An unexpected error occurred, please try again.")
            }
        }
    }

    private fun isValidEmailAndPassword(email: String, password: String): Boolean {
        return (email.isNotEmpty() && password.isNotEmpty()) &&
            password.contentEquals(et_confirm_password.text.toString())
    }
}