package com.konztic.chatzillo.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.konztic.chatzillo.R
import com.konztic.chatzillo.utilities.*
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        et_email.customValidation {
            et_email.error = if (isValidEmail(it)) null else "Email is not valid"
        }

        btn_go_login.setOnClickListener {
            goToActivity<LoginActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        btn_forgot.setOnClickListener {
            val email = et_email.text.toString()

            if(isValidEmail(email)) {
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(this) {
                    toast("Email has been sent to reset your  password")

                    goToActivity<LoginActivity> {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
            } else {
                toast("Please make your sure the email address is correct")
            }
        }
    }
}