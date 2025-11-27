package com.n1ck120.easydoc.activities

import android.content.Intent
import android.os.Bundle
import android.widget.GridLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.button.MaterialButton
import com.n1ck120.easydoc.R
import com.n1ck120.easydoc.adapters.ModelsAdapter
import com.n1ck120.easydoc.core.document.DocumentModels
import com.n1ck120.easydoc.database.datastore.SettingsDataStore
import com.n1ck120.easydoc.utils.DialogBuilder
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

class ModelsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val datdaStore = SettingsDataStore.getDataStorePrefs(this)
        val m3colors = booleanPreferencesKey("m3colors")
        lifecycleScope.launch {
            runBlocking {
                if (datdaStore.data.first()[m3colors] ?: false){
                    setTheme(com.google.android.material.R.style.Theme_Material3_DynamicColors_DayNight_NoActionBar)
                }
            }
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_models)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //Declaração de variaveis globais
        val backBtn = findViewById<MaterialButton>(R.id.backButton)
        val helpBtn = findViewById<MaterialButton>(R.id.helpButton)
        //Instanciando DialogBuilder
        val dialog = DialogBuilder(this)

        backBtn.setOnClickListener {
            finish()
        }

        helpBtn.setOnClickListener {
            dialog.genericDialog(getString(R.string.help),
                getString(R.string.help_models),this,getString(R.string.understood),null)
        }

        val jsonString = assets.open("doc_models.json").bufferedReader().use { it.readText() }
        val documentModels = Json.decodeFromString<DocumentModels>(jsonString)
        val docModel = documentModels.documents

        val dataset1 = docModel
        val dataset2 = docModel
        val modelsAdapter = ModelsAdapter(dataset1, dataset2, { docPosition ->
            val intent = Intent(this, ModelEditorActivity::class.java)
            intent.putExtra("Data", docPosition)
            startActivity(intent)
        })

        val recyclerView: RecyclerView = findViewById(R.id.recycler_models)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, GridLayout.VERTICAL)
        recyclerView.adapter = modelsAdapter
    }
}