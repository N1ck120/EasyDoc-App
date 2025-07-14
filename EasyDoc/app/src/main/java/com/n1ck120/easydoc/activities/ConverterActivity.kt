package com.n1ck120.easydoc.activities

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.n1ck120.easydoc.R
import com.n1ck120.easydoc.utils.DialogBuilder

class ConverterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_converter)
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
            dialog.genericDialog("Ajuda","O conversor de arquivos permite converter documentos entre os formatos DOCX e PDF, e vice-versa.\n\nObs.: Dependendo da formatação do PDF, a conversão para DOCX pode não ser perfeita.",this, "Entendi", null)
        }
    }
}