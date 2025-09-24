package com.n1ck120.easydoc.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.google.android.material.button.MaterialButton
import com.n1ck120.easydoc.R
import com.n1ck120.easydoc.core.document.DocModel
import com.n1ck120.easydoc.core.document.DocumentGen
import com.n1ck120.easydoc.core.document.DocumentModels
import com.n1ck120.easydoc.database.room.AppDatabase
import com.n1ck120.easydoc.database.room.Doc
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.apache.poi.hslf.util.SystemTimeUtils.getDate
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.text.contains

class ModelEditorActivity : AppCompatActivity() {

    lateinit var db : AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_model_editor)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database.db"
        ).build()

        val linear = findViewById<LinearLayout>(R.id.linear)
        val jsonString = assets.open("doc_models.json").bufferedReader().use { it.readText() }
        val documentModels = Json.Default.decodeFromString<DocumentModels>(jsonString)
        val docModel = documentModels.documents[intent.getIntExtra("Data", 0)]
        //Declaração de variaveis globais
        val title = findViewById<TextView>(R.id.modelTitle)
        val backBtn = findViewById<MaterialButton>(R.id.backButton)
        val format = findViewById<RadioGroup>(R.id.groupType)
        val save = findViewById<MaterialButton>(R.id.save)
        val export = findViewById<MaterialButton>(R.id.generatedoc)
        val share = findViewById<MaterialButton>(R.id.share)
        findViewById<RadioGroup>(R.id.groupType)

        title.text = docModel.title

        fun validField(field : EditText): Boolean {
            if (field.text.isNullOrBlank()){
                field.error = this.getString(R.string.mandatory_field)
                return false
            }else{
                if (field.text.contains(Regex("[\\p{So}\\p{Cs}]"))){
                    field.error = this.getString(R.string.invalid_characters)
                    return false
                }
            }
            return true
        }

        backBtn.setOnClickListener {
            finish()
        }

        val fieldList = mutableListOf<String>()
        for (prop in DocModel::class.memberProperties) {
            fieldList.add(prop.name)
        }

        fun getString(search : String): String {
            var count = 0
            var val2 : String = ""
            while (count < fieldList.size) {
                val property = docModel::class.memberProperties.find { it.name == fieldList[count] } as? KProperty1<Any, *>
                val propName = property?.name.toString()
                val valor = property?.get(docModel)
                if (valor is String && propName == search) {
                    val2 = valor
                }
                count++
            }
            return val2
        }

        fun iterateModel(classe: Any): MutableList<Int> {
            var count = 0
            val ids = mutableListOf<Int>()
            while (count < fieldList.size) {
                val property = classe::class.memberProperties.find { it.name == fieldList[count] } as? KProperty1<Any, *>
                val propName = property?.name.toString()
                val valor = property?.get(classe)
                if (valor is String && valor.isEmpty()) {
                    val textField = EditText(this)
                    textField.id = count
                    ids.add(textField.id)
                    textField.hint = propName.replaceFirst(propName.first(), propName.first().uppercaseChar())
                    linear.addView(textField)
                }
                count++
            }
            return ids
        }

        val ids = iterateModel(docModel)

        save.setOnClickListener {
            var count = 0
            var paragraphText = ""
            var aaa = getString("content")
            while (count < ids.size) {
                val id = ids[count]
                val a = findViewById<EditText>(id)
                if (!validField(a)){
                    break
                }
                while (aaa.contains("{${a.hint.toString().replace(a.hint.first(), a.hint.first().lowercaseChar())}}")){
                    aaa = aaa.replace("{${a.hint.toString().replace(a.hint.first(), a.hint.first().lowercaseChar())}}", a.text.toString())
                }
                count++
                paragraphText = aaa
            }
            if (count == ids.size){
                val dox = Doc(
                    doc_name = (docModel.title.replace(" ","_")),
                    title = docModel.title,
                    content = paragraphText,
                    date = "getDate()"
                )

                val a = lifecycleScope.launch {
                    db.userDao().insertAll(dox)
                }
                a.invokeOnCompletion {
                    Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show()
                }
            }
        }

        export.setOnClickListener {
            var count = 0
            var paragraphText = ""
            var aaa = getString("content")
            while (count < ids.size) {
                val id = ids[count]
                val a = findViewById<EditText>(id)
                if (!validField(a)){
                    break
                }
                while (aaa.contains("{${a.hint.toString().replace(a.hint.first(), a.hint.first().lowercaseChar())}}")){
                    aaa = aaa.replace("{${a.hint.toString().replace(a.hint.first(), a.hint.first().lowercaseChar())}}", a.text.toString())
                }
                count++
                paragraphText = aaa
            }
            if (count == ids.size){
                DocumentGen.docGenerator(docModel.title, paragraphText, (docModel.title.replace(" ","_")), findViewById<RadioButton>(format.checkedRadioButtonId).text.toString().lowercase(), this)
            }
        }

        share.setOnClickListener {
            var count = 0
            var paragraphText = ""
            var aaa = getString("content")
            while (count < ids.size) {
                val id = ids[count]
                val a = findViewById<EditText>(id)
                if (!validField(a)){
                    break
                }
                while (aaa.contains("{${a.hint.toString().replace(a.hint.first(), a.hint.first().lowercaseChar())}}")){
                    aaa = aaa.replace("{${a.hint.toString().replace(a.hint.first(), a.hint.first().lowercaseChar())}}", a.text.toString())
                }
                count++
                paragraphText = aaa
            }
            if (count == ids.size){
                val uri = DocumentGen.docGenerator(docModel.title, paragraphText, (docModel.title.replace(" ","_")), findViewById<RadioButton>(format.checkedRadioButtonId).text.toString().lowercase(), this)
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "application/pdf"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }

                // Iniciar o chooser
                this.startActivity(Intent.createChooser(shareIntent,
                    this.getString(R.string.share_via)))
            }
        }
    }
}
