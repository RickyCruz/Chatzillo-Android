package com.konztic.chatzillo.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.konztic.chatzillo.R
import com.konztic.chatzillo.utilities.goToActivity
import com.konztic.chatzillo.utilities.toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login.setOnClickListener {
            val email = et_email.text.toString()
            val password = et_password.text.toString()

            if (isValidEmailAndPassword(email, password)) {
                logInByEmail(email, password)
            } else {
                toast("Please fill all data and confirm that your password is correct")
            }
        }

        tv_forgot_password.setOnClickListener {
            goToActivity<ForgotPasswordActivity>()
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

        btn_create_account.setOnClickListener {
            goToActivity<SignUpActivity>()
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }
    }

    private fun logInByEmail(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                toast("User is now logged in")
            } else {
                toast("An unexpected error occurred, please try again.")
            }
        }
    }

    private fun isValidEmailAndPassword(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }
}