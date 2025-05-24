package com.n1ck120.easydoc

import android.content.Intent
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.lifecycleScope
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
        val email = findViewById<EditText>(R.id.emailField)
        val pass1 = findViewById<EditText>(R.id.pass1)
        val pass2 = findViewById<EditText>(R.id.pass2)
        val message = findViewById<TextView>(R.id.textView10)

        btnLogin.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        email.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus && !email.text.toString().contains(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))){
                email.error = "Email inválido!"
            }
        }

        pass1.doAfterTextChanged {
            if (pass1.text.toString().length < 8){
                message.visibility = VISIBLE
                message.text = "A senha deve conter ao menos 8 caracteres"
            }else{
                if (pass1.text.isNullOrBlank() || pass1.text.toString().contains(" ")){
                    message.visibility = VISIBLE
                    message.text = "Senha inválida a senha não pode conter espaços"
                }else{
                    message.visibility = INVISIBLE
                }
            }
        }

        pass2.doAfterTextChanged {
            if (pass1.text.toString() != pass2.text.toString()){
                message.visibility = VISIBLE
                message.text = "As senhas não coincidem!"
            }else{
                message.visibility = INVISIBLE
            }
        }

        btnSignup.setOnClickListener {
            if (!email.text.toString().contains(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))){
                Toast.makeText(this, "Email inválido!", Toast.LENGTH_SHORT).show()
            }else{
                if (pass1.text.toString().length < 8){
                    Toast.makeText(this, "A senha deve conter ao menos 8 caracteres", Toast.LENGTH_SHORT).show()
                }else{
                    if (pass1.text.isNullOrBlank() || pass1.text.toString().contains(" ")){
                        Toast.makeText(this, "Senha inválida a senha não pode conter espaços", Toast.LENGTH_SHORT).show()
                    }else{
                        if (pass1.text.toString() != pass2.text.toString()){
                            Toast.makeText(this, "As senhas não coincidem!", Toast.LENGTH_SHORT).show()
                        }else{
                            //TODO() Função de cadastro aqui
                            Toast.makeText(this, "Cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this,LoginActivity::class.java)
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
            AppCompatDelegate.setDefaultNightMode(dataStore.data.first()[key] ?: MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}