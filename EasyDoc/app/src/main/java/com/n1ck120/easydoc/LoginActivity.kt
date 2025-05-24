package com.n1ck120.easydoc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.lifecycleScope
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
        val pass = findViewById<EditText>(R.id.textPass)

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

        //Verifica o tema salvo no datastore e troca caso necessario
        val dataStore = SettingsDataStore.getDataStorePrefs(this)
        val key = intPreferencesKey("theme")
        lifecycleScope.launch {
            AppCompatDelegate.setDefaultNightMode(dataStore.data.first()[key] ?: MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}