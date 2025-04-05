package com.n1ck120.easydoc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnSignup = findViewById<Button>(R.id.btnSignup)
        val btnOffline = findViewById<Button>(R.id.btnReturn)
        val checkKeep = findViewById<CheckBox>(R.id.checkBox)

        btnSignup.setOnClickListener {
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnOffline.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            /*if (checkKeep.isChecked){
                Toast.makeText(this, "Checkbox marcado", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Checkbox não marcado", Toast.LENGTH_SHORT).show()
            }*/
            finish()
        }
    }
}