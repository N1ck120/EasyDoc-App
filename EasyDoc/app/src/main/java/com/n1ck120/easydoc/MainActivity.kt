package com.n1ck120.easydoc

import android.app.AlertDialog
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import androidx.fragment.app.commit
import com.itextpdf.layout.element.Paragraph
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val createDoc = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        val account = findViewById<ImageButton>(R.id.btnAccount)
        val docs = findViewById<ImageButton>(R.id.btnDocs)
        val settings = findViewById<ImageButton>(R.id.btnSettings)
        val home = findViewById<ImageButton>(R.id.btnHome)

        createDoc.setOnClickListener {
            createDocDialog()
        }

        home.setOnClickListener {
            supportFragmentManager.commit {
                setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                replace(R.id.fragmentContainerView, HomeFragment())
            }
        }

        docs.setOnClickListener {
            supportFragmentManager.commit {
                setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                replace(R.id.fragmentContainerView, DocsFragment())
            }
        }

        account.setOnClickListener {
            supportFragmentManager.commit {
                setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                replace(R.id.fragmentContainerView, AccountFragment())
            }
        }

        settings.setOnClickListener {
            supportFragmentManager.commit {
                setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                replace(R.id.fragmentContainerView, SettingsFragment())
            }
        }
    }

    private fun contentVer(a: TextInputEditText, b: Boolean = false) : String{
        if (a.text.isNullOrBlank() && !b){
            return ""
        } else if (a.text.isNullOrBlank() && b){
            return "Exemplo"
        } else{
            return  a.text.toString()
        }
    }

    private fun createDocDialog(){
        val dialogView = LayoutInflater.from(this).inflate(R.layout.create_doc, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()
        val titleDoc = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.title)
        val contentDoc = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.content)
        val workerDoc = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.worker)
        val outputDoc = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.outputname)
        val generateBtn = dialogView.findViewById<Button>(R.id.generatedoc)

        generateBtn.setOnClickListener {
            // Define o caminho para salvar o PDF na pasta Downloads
            val pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/"+contentVer(outputDoc, true)+".pdf"
            Toast.makeText(this, "Salvo em: $pdfPath", Toast.LENGTH_LONG).show()
            val file = File(pdfPath)


            val writer = PdfWriter(file)
            val pdfDoc = PdfDocument(writer)
            val document = Document(pdfDoc)

            document.setFontSize(20F)
            document.add(Paragraph(contentVer(titleDoc)))

            document.setFontSize(12F)
            document.add(Paragraph(contentVer(contentDoc)))

            document.setFontSize(12F)
            document.add(Paragraph(contentVer(workerDoc)))

            document.close()
            dialog.dismiss()
        }
        dialog.show()
    }
}