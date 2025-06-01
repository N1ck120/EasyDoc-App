package com.n1ck120.easydoc

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.serialization.json.Json

class DocEditorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_doc_editor)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val jsonString = assets.open("doc_models.json").bufferedReader().use { it.readText() }
        val documentModels = Json.decodeFromString<DocumentModels>(jsonString)
        val docModel = documentModels.documents[intent.getIntExtra("Data", 0)]

        val teste = findViewById<TextView>(R.id.modelTitle)
        val backBtn = findViewById<Button>(R.id.backButton)

        backBtn.setOnClickListener {
            finish()
        }

        teste.text = docModel.title

    }
}