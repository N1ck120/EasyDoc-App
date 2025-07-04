package com.n1ck120.easydoc.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.n1ck120.easydoc.core.document.DocumentModels
import com.n1ck120.easydoc.adapters.ModelsAdapter
import com.n1ck120.easydoc.R
import kotlinx.serialization.json.Json

class ModelsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_models)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val backBtn = findViewById<Button>(R.id.backButton)

        backBtn.setOnClickListener {
            finish()
        }

        val jsonString = assets.open("doc_models.json").bufferedReader().use { it.readText() }
        val documentModels = Json.Default.decodeFromString<DocumentModels>(jsonString)
        val docModel = documentModels.documents


        val dataset1 = docModel
        val dataset2 = docModel
        val modelsAdapter = ModelsAdapter(dataset1, dataset2, { docPosition ->
            val intent = Intent(this, DocEditorActivity::class.java)
            intent.putExtra("Data", docPosition)
            startActivity(intent)
        })

        val recyclerView: RecyclerView = findViewById(R.id.recycler_models)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, GridLayout.VERTICAL)
        recyclerView.adapter = modelsAdapter
    }
}