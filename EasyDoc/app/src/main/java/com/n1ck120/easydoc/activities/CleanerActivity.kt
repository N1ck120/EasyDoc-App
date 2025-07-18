package com.n1ck120.easydoc.activities

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.n1ck120.easydoc.R
import com.n1ck120.easydoc.utils.DialogBuilder

class CleanerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cleaner)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val backBtn = findViewById<Button>(R.id.backButton)
        val helpBtn = findViewById<Button>(R.id.helpButton)

        val dialog = DialogBuilder(this, {},{},{})

        backBtn.setOnClickListener {
            finish()
        }

        helpBtn.setOnClickListener {
            dialog.genericDialog("Ajuda","Metadados incluem informações como quem criou o documento, quando e com qual aplicação. Essas informações podem ser críticas em alguns contextos. O limpador de metadados as remove para melhorar sua privacidade.", this, "Entendi", null)
        }
    }
}