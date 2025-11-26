package com.n1ck120.easydoc.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.icu.text.IDNA
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.color.DynamicColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.n1ck120.easydoc.R
import com.n1ck120.easydoc.database.datastore.SettingsDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.log

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val dataStore = SettingsDataStore.getDataStorePrefs(this)
        val m3colors = booleanPreferencesKey("m3colors")
        lifecycleScope.launch {
            runBlocking {
                if (dataStore.data.first()[m3colors] ?: false){
                    setTheme(com.google.android.material.R.style.Theme_Material3_DynamicColors_DayNight_NoActionBar)
                }
            }
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //Declaração de variaveis globais
        val btnLogin = findViewById<MaterialButton>(R.id.btnLogin)
        val btnSignup = findViewById<MaterialButton>(R.id.btnSignup)
        val btnOffline = findViewById<MaterialButton>(R.id.btnReturn)
        //val checkKeep = findViewById<CheckBox>(R.id.checkBox)
        val loginEmail = findViewById<EditText>(R.id.loginEmail)
        val loginPass = findViewById<EditText>(R.id.loginPass)
        //Declaração de variaveis relacionadas ao dataStore

        val key = intPreferencesKey("theme")
        val offlineMode = intPreferencesKey("offlineMode")
        val accepted = booleanPreferencesKey("accepted")

        val intent = Intent(this, MainActivity::class.java)

        btnSignup.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        loginEmail.doAfterTextChanged {
            if (!loginEmail.text.toString().contains(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))){
                loginEmail.error = getString(R.string.invalid_email)
            }
        }

        btnLogin.setOnClickListener {
            if (!loginEmail.text.toString().contains(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))){
                Toast.makeText(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT).show()
            }else{
                if (loginPass.text.toString().isBlank() || loginPass.text.toString().length < 8 || loginPass.text.toString().contains(" ")){
                    Toast.makeText(this, getString(R.string.invalid_password), Toast.LENGTH_SHORT).show()
                }else{
                    //TODO() Função de login aqui
                }
            }
        }

        intPreferencesKey("userAgreement")

        val dialogView = LayoutInflater.from(this).inflate(R.layout.eula_dialog, null)
        val a = dialogView.findViewById<MaterialButton>(R.id.button3)
        val b = dialogView.findViewById<CheckBox>(R.id.checkBox3)
        val dialog = MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .setTitle(getString(R.string.terms_of_use))
            .setCancelable(false)
            .create()

        b.setOnCheckedChangeListener { buttonView, isChecked ->
            a.isEnabled = isChecked
        }
        a.setOnClickListener {
            lifecycleScope.launch {
                dataStore.edit { preferences ->
                    preferences[accepted] = true
                    dialog.dismiss()
                }
            }
        }
        lifecycleScope.launch {
            if (!(dataStore.data.first()[accepted] ?: false)){
                dialog.show()
            }
        }

        btnOffline.setOnClickListener {
            startActivity(intent)
            finish()
        }

        //Verifica o tema salvo no datastore e troca caso necessario
        lifecycleScope.launch {
            AppCompatDelegate.setDefaultNightMode(dataStore.data.first()[key] ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            if ((dataStore.data.first()[offlineMode] ?: 0) == 1){
                startActivity(intent)
                finish()
            }
        }
    }
}