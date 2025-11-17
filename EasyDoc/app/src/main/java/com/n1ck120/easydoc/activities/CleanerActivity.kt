package com.n1ck120.easydoc.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.util.Log
import android.util.Log.ERROR
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.n1ck120.easydoc.R
import com.n1ck120.easydoc.core.document.DocumentUtils
import com.n1ck120.easydoc.utils.DialogBuilder

class CleanerActivity : AppCompatActivity() {
    var filename1 : String? = null
    var fileMime : String? = null
    lateinit var file : MaterialCardView
    lateinit var scroll : ScrollView
    lateinit var linearl : LinearLayout
    lateinit var clearBtn : MaterialButton

    lateinit var title : TextView

    val doc = DocumentUtils

    private val pickFile = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            val projection = arrayOf(DocumentsContract.Document.COLUMN_DISPLAY_NAME)
            val cursor = contentResolver.query(selectedUri, projection, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val fileName = it.getString(0)
                    filename1 = fileName
                }
            }
            contentResolver.takePersistableUriPermission(selectedUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val inputStream = contentResolver.openInputStream(selectedUri)
            val mime = contentResolver.getType(selectedUri)
            fileMime = mime
            try {
                inputStream?.use { stream ->
                    file.visibility = GONE
                    scroll.visibility = VISIBLE
                    linearl.visibility = VISIBLE
                    title.text = getString(R.string.found_metadata)
                    var i = 0
                    var text = ""
                    val list = doc.docInfo(stream, fileMime)
                    while (list.size > i){
                        text += list[i]+"\n\n"
                        i++
                    }
                    findViewById<TextView>(R.id.textView14).text = text
                }
                clearBtn.setOnClickListener {
                    var i = 0
                    var text = ""
                    val list = doc.docClean(selectedUri, fileMime, this)
                    list?.size?.let { it1 ->
                        while (it1 > i) {
                            text += list[i] + "\n\n"
                            i++
                        }
                    }
                    findViewById<TextView>(R.id.textView14).text = text
                }
            }catch (e: Exception){
                file.visibility = VISIBLE
                scroll.visibility = GONE
                linearl.visibility = GONE
                Toast.makeText(this, getString(R.string.invalid_file), Toast.LENGTH_SHORT).show()
                //Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
                Log.println(ERROR,null, e.toString())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cleaner)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //Declaração de variaveis globais
        val backBtn = findViewById<MaterialButton>(R.id.backButton)
        val helpBtn = findViewById<MaterialButton>(R.id.helpButton)
        file = findViewById<MaterialCardView>(R.id.selectFile)
        val file2 = findViewById<MaterialButton>(R.id.selectFile2)
        clearBtn = findViewById<MaterialButton>(R.id.clearBtn)
        scroll = findViewById<ScrollView>(R.id.scrollView2)
        linearl = findViewById<LinearLayout>(R.id.layoutBtns)
        title = findViewById<TextView>(R.id.documentText)
        //Instanciando DialogBuilder
        val dialog = DialogBuilder(this)

        file.setOnClickListener {
            pickFile.launch(arrayOf("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/pdf"))
        }

        file2.setOnClickListener {
            pickFile.launch(arrayOf("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/pdf"))
        }

        backBtn.setOnClickListener {
            finish()
        }

        helpBtn.setOnClickListener {
            dialog.genericDialog(getString(R.string.help),
                getString(R.string.help_metadata_cleaner), this,
                getString(R.string.understood), null)
        }
    }
}