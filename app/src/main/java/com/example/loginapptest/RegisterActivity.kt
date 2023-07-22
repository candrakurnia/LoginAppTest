package com.example.loginapptest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.loginapptest.databinding.ActivityRegisterBinding
import com.example.loginapptest.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterBinding
    lateinit var auth : FirebaseAuth
    private lateinit var database : DatabaseReference
    private lateinit var sName : String
    private lateinit var sEmail : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference

        nameFocusListener()
        emailFocusListener()
        passFocusListener()
        confirmFocusListener()

        binding.apply {
            tvLogin.setOnClickListener {
                intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            btnRegister.setOnClickListener {
                submitData()
            }
        }

    }

    private fun submitData() {
        binding.tvContainerConf.helperText = validConfirm()

        val email = binding.tvInputEmail.text.toString()
        val pass = binding.tvInputPass.text.toString()
        val validName = binding.tvContainerName.helperText == null
        val validEmail = binding.tvContainerEmail.helperText == null
        val validPass = binding.tvContainerPass.helperText == null
        val validConfirm = binding.tvContainerConf.helperText == null

        if (validName && validEmail && validPass && validConfirm) {
            RegisterFirebase(email,pass)
        }
        else {
            Toast.makeText(this, "Terdapat data yang salah", Toast.LENGTH_SHORT).show()
        }

    }


    private fun nameFocusListener() {
        binding.tvInputName.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.tvContainerName.helperText = validName()
            }
        }
    }

    private fun validName(): String? {
        val name = binding.tvInputName.text.toString()
        if (name.isEmpty()) {
            return "Nama tidak boleh kosong!"

        }
        if (name.length < 3) {
            return "Nama terlalu pendek!"

        }
        if (name.length > 50) {
            return "Maksimal 50 karakter!"

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
        if (pass.isEmpty()) {
            return "Password tidak boleh kosong!"

        }
        if (pass.length < 8) {
            return "Password Tidak boleh kurang dari 8 karakter!"

        }
        if (!pass.matches(".*[A-Z].*".toRegex())){
            return "Password harus mengandung 1 huruf besar, 1 huruf kecil, 1 angka!"

        }
        if (!pass.matches(".*[a-z].*".toRegex())){
            return  "Password harus mengandung 1 huruf besar, 1 huruf kecil, 1 angka!"

        }
        if (!pass.matches(".*[1-9].*".toRegex())){
            return "Password harus mengandung 1 huruf besar, 1 huruf kecil, 1 angka!"

        }

        return null
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

    private fun confirmFocusListener() {
        binding.tvInputConf.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.tvContainerConf.helperText = validConfirm()
            }
        }
    }

    private fun validConfirm(): String? {
        val confirm = binding.tvInputConf.text.toString()
        val pass = binding.tvInputPass.text.toString()
        if (confirm.isEmpty()) {
            return "Password tidak boleh kosong!"
        }
        if (confirm != pass) {
            return "Password harus sama!"

        }
        return null
    }


    private fun RegisterFirebase(email: String, pass: String) {
        auth.createUserWithEmailAndPassword(email,pass)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    auth.currentUser?.sendEmailVerification()
                        ?.addOnSuccessListener {
                            Toast.makeText(this, "Please Check ur email", Toast.LENGTH_SHORT).show()
                            saveData()
                            intent = Intent(this@RegisterActivity,LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        ?.addOnFailureListener { it ->
                            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                        }

                } else {
                    Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveData() {
        sEmail = binding.tvInputEmail.text.toString()
        sName = binding.tvInputName.text.toString()

        val uId = FirebaseAuth.getInstance().currentUser!!.uid

        val user = User(sName,sEmail)
        database.child("Data User").child(uId).setValue(user)
    }
}