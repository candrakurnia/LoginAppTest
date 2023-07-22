package com.example.loginapptest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.loginapptest.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        emailFocusListener()
        passFocusListener()

        binding.apply {
            tvForgot.setOnClickListener {
                intent = Intent(this@LoginActivity, ResetActivity::class.java)
                startActivity(intent)
                finish()
            }
            tvRegist.setOnClickListener {
                intent = Intent(this@LoginActivity,RegisterActivity::class.java)
                startActivity(intent)
            }
            btnLogin.setOnClickListener {
                submitLogin()
            }
        }
    }

    private fun submitLogin() {
        binding.tvContainerPass.helperText = validPass()
        val email = binding.tvInputEmail.text.toString()
        val pass = binding.tvInputPass.text.toString()
        val validEmail = binding.tvContainerEmail.helperText == null
        val validPass = binding.tvContainerPass.helperText == null

        if (validEmail && validPass) {
            SignInFirebase(email,pass)
        }
        else {
            Toast.makeText(this, "Terdapat data yang salah", Toast.LENGTH_SHORT).show()
        }
    }

    private fun emailFocusListener() {
        binding.tvInputEmail.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.tvContainerEmail.helperText = validEmail()
            }
        }
    }

    private fun validEmail(): String? {
        val email = binding.tvInputEmail.text.toString()
        if (email.isEmpty()) {
            return  "Email tidak boleh kosong!"
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "Email tidak valid!"
        }
        return null
    }

    private fun passFocusListener() {
        binding.tvInputPass.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.tvContainerPass.helperText = validPass()
            }
        }
    }

    private fun validPass(): String? {
        val pass = binding.tvInputPass.text.toString()
        if (!pass.matches(".*[A-Z].*".toRegex())){
            return "Password harus mengandung 1 huruf besar!"

        }
        if (!pass.matches(".*[a-z].*".toRegex())){
            return  "Password harus mengandung 1 huruf kecil!"

        }
        if (!pass.matches(".*[1-9].*".toRegex())){
            return "Password harus mengandung 1 angka!"

        }
        if (pass.isEmpty()) {
            return "Password tidak boleh kosong!"

        }
        if (pass.length < 8) {
            return "Password Tidak boleh kurang dari 8 karakter!"

        }
        return null
    }

    private fun SignInFirebase(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email,pass)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Berhasil Login", Toast.LENGTH_SHORT).show()
                    intent = Intent(this@LoginActivity,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            intent = Intent(this@LoginActivity,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}