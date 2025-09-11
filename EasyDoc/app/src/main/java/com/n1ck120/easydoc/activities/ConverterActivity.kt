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
        //Declaração de variaveis globais
        val backBtn = findViewById<Button>(R.id.backButton)
        val helpBtn = findViewById<Button>(R.id.helpButton)
        //Instanciando DialogBuilder
        val dialog = DialogBuilder(this)

        backBtn.setOnClickListener {
            finish()
        }

        helpBtn.setOnClickListener {
            dialog.genericDialog(getString(R.string.help),
                getString(R.string.help_converter),this, getString(R.string.understood), null)
        }
    }
}