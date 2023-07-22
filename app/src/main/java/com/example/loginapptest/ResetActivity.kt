package com.example.loginapptest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.loginapptest.databinding.ActivityResetBinding
import com.google.firebase.auth.FirebaseAuth

class ResetActivity : AppCompatActivity() {
    lateinit var binding: ActivityResetBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnReset.setOnClickListener {
            val pass = binding.tvInputEmail.text.toString()
            auth.sendPasswordResetEmail(pass)
                .addOnSuccessListener {
                    Toast.makeText(this,"Please Check ur Email", Toast.LENGTH_SHORT).show()
                    intent = Intent(this@ResetActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this,it.toString(),Toast.LENGTH_SHORT).show()
                }
        }
    }
}