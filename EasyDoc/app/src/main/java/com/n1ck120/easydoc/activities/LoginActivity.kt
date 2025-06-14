package com.n1ck120.easydoc.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.n1ck120.easydoc.R
import com.n1ck120.easydoc.activities.SignUpActivity
import com.n1ck120.easydoc.core.crypto.SodiumLazy
import com.n1ck120.easydoc.database.datastore.SettingsDataStore
import io.jsonwebtoken.Jwt
import io.jsonwebtoken.Jwts
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

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
        val loginEmail = findViewById<EditText>(R.id.loginEmail)
        val loginPass = findViewById<EditText>(R.id.loginPass)
        val intent = Intent(this, MainActivity::class.java)

        val lzSodium = SodiumLazy().lazySodium.sodium

        btnSignup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        loginEmail.doAfterTextChanged {
            if (!loginEmail.text.toString().contains(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))){
                loginEmail.error = "Email inválido!"
            }
        }

        btnLogin.setOnClickListener {
            if (!loginEmail.text.toString().contains(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))){
                Toast.makeText(this, "Email inválido!", Toast.LENGTH_SHORT).show()
            }else{
                if (loginPass.text.toString().isBlank() || loginPass.text.toString().length < 8 || loginPass.text.toString().contains(" ")){
                    Toast.makeText(this, "Senha inválida!", Toast.LENGTH_SHORT).show()
                }else{
                    if (false){
                        /*TODO() Função de login aqui
                        loginEmail.text.toString().lowercase()
                        */
                    }else{
                        Snackbar.make(it,"Não há conexão com a internet",Snackbar.LENGTH_LONG)
                            .setAction("Tentar novamente"){
                            }
                            .show()
                    }
                }
            }
        }

        btnOffline.setOnClickListener {
            startActivity(intent)
            /*if (checkKeep.isChecked){
                Toast.makeText(this, "Checkbox marcado", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Checkbox não marcado", Toast.LENGTH_SHORT).show()
            }*/
            finish()
        }

        //Verifica o tema salvo no datastore e troca caso necessario
        val dataStore = SettingsDataStore.getDataStorePrefs(this)
        val key = intPreferencesKey("theme")
        val offlineMode = intPreferencesKey("offlineMode")
        lifecycleScope.launch {
            AppCompatDelegate.setDefaultNightMode(dataStore.data.first()[key] ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            if ((dataStore.data.first()[offlineMode] ?: 0) == 1){
                startActivity(intent)
                finish()
            }
        }
    }
}