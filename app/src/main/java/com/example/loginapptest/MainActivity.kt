package com.example.loginapptest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.loginapptest.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var database: DatabaseReference
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.database.reference
        auth = FirebaseAuth.getInstance()


        val uId = FirebaseAuth.getInstance().currentUser!!.uid
        val verification = auth.currentUser!!.isEmailVerified.toString()
        binding.tvVerif.text = verification


        database.child("Data User").child(uId).get().addOnSuccessListener {
            val name = it.child("name").value.toString()
            val email = it.child("email").value.toString()

            binding.textView3.text = name
            binding.tvEmail.text = email
        }.addOnFailureListener {
            Toast.makeText(this,it.toString(), Toast.LENGTH_SHORT).show()
        }

        binding.btnLogout.setOnClickListener {
            auth.signOut()
            intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}