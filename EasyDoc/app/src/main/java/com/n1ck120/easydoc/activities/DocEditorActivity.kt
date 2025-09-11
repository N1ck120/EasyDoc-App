package com.n1ck120.easydoc.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.n1ck120.easydoc.core.document.DocModel
import com.n1ck120.easydoc.core.document.DocumentModels
import com.n1ck120.easydoc.R
import com.n1ck120.easydoc.utils.DialogBuilder
import kotlinx.serialization.json.Json
import java.util.UUID
import kotlin.random.Random
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

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

        val linear = findViewById<LinearLayout>(R.id.linear)
        val jsonString = assets.open("doc_models.json").bufferedReader().use { it.readText() }
        val documentModels = Json.Default.decodeFromString<DocumentModels>(jsonString)
        val docModel = documentModels.documents[intent.getIntExtra("Data", 0)]

        val title = findViewById<TextView>(R.id.modelTitle)
        val backBtn = findViewById<Button>(R.id.backButton)
        val save = findViewById<Button>(R.id.save)
        val export = findViewById<Button>(R.id.generatedoc)
        val format = findViewById<RadioGroup>(R.id.groupType)

        title.text = docModel.title

        backBtn.setOnClickListener {
            finish()
        }

        val fieldList = mutableListOf<String>()
        for (prop in DocModel::class.memberProperties) {
            fieldList.add(prop.name)
        }

        fun iterateModel(classe : Any): MutableList<Int> {
            var count = 0
            val ids = mutableListOf<Int>()
            while (count < fieldList.size) {
                val property = classe::class.memberProperties.find { it.name == fieldList[count] } as? KProperty1<Any, *>
                val propName = property?.name.toString()
                val valor = property?.get(classe)
                if (valor is String && propName != "title" && propName != "type" && propName != "description"){
                    val textField = EditText(this)
                    textField.id = Random.nextInt()
                    ids.add(textField.id)
                    textField.hint = propName.replaceFirst(propName.first(), propName.first().uppercaseChar()).replace("_", " ")
                    linear.addView(textField)
                }
                count++
            }
            return ids
        }

        val ids = iterateModel(docModel)

        save.setOnClickListener {
            var count = 0
            var b = ""
            while (count < ids.size) {
                val id = ids[count]
                val a = findViewById<EditText>(id)
                b = b + a.text.toString()
                count++
            }
            Toast.makeText(this, b, Toast.LENGTH_LONG).show()
        }


        export.setOnClickListener {
            var count = 0
            var b = ""
            while (count < ids.size) {
                val id = ids[count]
                val a = findViewById<EditText>(id)
                b = b + a.text.toString()
                count++
            }
            Toast.makeText(this, b, Toast.LENGTH_LONG).show()
        }

    }
}