package com.n1ck120.easydoc.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.n1ck120.easydoc.R
import com.n1ck120.easydoc.database.datastore.SettingsDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnSignup = findViewById<Button>(R.id.btnSignup)
        val signupEmail = findViewById<EditText>(R.id.signupEmail)
        val signupPass1 = findViewById<EditText>(R.id.signupPass1)
        val signupPass2 = findViewById<EditText>(R.id.signupPass2)
        val message = findViewById<TextView>(R.id.textView10)

        btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        signupEmail.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus && !signupEmail.text.toString().contains(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))){
                signupEmail.error = getString(R.string.email_invalid)
            }
        }

        signupPass1.doAfterTextChanged {
            if (signupPass1.text.toString().length < 8){
                message.visibility = View.VISIBLE
                message.text = getString(R.string.invalid_password_length)
            }else{
                if (signupPass1.text.isNullOrBlank() || signupPass1.text.toString().contains(" ")){
                    message.visibility = View.VISIBLE
                    message.text = getString(R.string.invalid_password_character)
                }else{
                    message.visibility = View.INVISIBLE
                }
            }
        }

        signupPass2.doAfterTextChanged {
            if (signupPass1.text.toString() != signupPass2.text.toString()){
                message.visibility = View.VISIBLE
                message.text = getString(R.string.password_mismatch)
            }else{
                message.visibility = View.INVISIBLE
            }
        }

        btnSignup.setOnClickListener {
            if (!signupEmail.text.toString().contains(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))){
                Toast.makeText(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT).show()
            }else{
                if (signupPass1.text.toString().length < 8){
                    Toast.makeText(this, getString(R.string.invalid_password_length), Toast.LENGTH_SHORT).show()
                }else{
                    if (signupPass1.text.isNullOrBlank() || signupPass1.text.toString().contains(" ")){
                        Toast.makeText(this, getString(R.string.invalid_password_character), Toast.LENGTH_SHORT).show()
                    }else{
                        if (signupPass1.text.toString() != signupPass2.text.toString()){
                            Toast.makeText(this, getString(R.string.password_mismatch), Toast.LENGTH_SHORT).show()
                        }else{
                            //TODO() Função de cadastro aqui
                            Toast.makeText(this,
                                getString(R.string.successfully_registered), Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
        }

        //Verifica o tema salvo no datastore e troca caso necessario
        val dataStore = SettingsDataStore.getDataStorePrefs(this)
        val key = intPreferencesKey("theme")
        lifecycleScope.launch {
            AppCompatDelegate.setDefaultNightMode(dataStore.data.first()[key] ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}