package com.konztic.chatzillo.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.konztic.chatzillo.R
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity : AppCompatActivity() {
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        btn_go_login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        btn_sign_up.setOnClickListener {
            val email = et_email.text.toString()
            val password = et_password.text.toString()

            if (isValidEmailAndPassword(email, password)) {
                signUpByEmail(email, password)
            } else {
                Toast.makeText(this, "Please fill all data and confirm that your password is correct", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signUpByEmail(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "An email has been sent to you. Please, confirm before sign in", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "An unexpected error occurred, please try again.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValidEmailAndPassword(email: String?, password: String?): Boolean {

        return !email.isNullOrEmpty() ||
                !password.isNullOrEmpty() ||
                (password !== et_confirm_password.text.toString())
    }
}