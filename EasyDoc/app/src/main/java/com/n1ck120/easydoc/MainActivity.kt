package com.n1ck120.easydoc

import android.app.AlertDialog
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.*
import java.io.File
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph

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

        /*fun contentVer(){
            if (){

            }
        }*/
        
        fun createDocDialog(){
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
                val pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/"+outputDoc.text.toString()+".pdf"
                val file = File(pdfPath)

                val writer = PdfWriter(file)
                val pdfDoc = PdfDocument(writer)
                val document = Document(pdfDoc)

                document.setFontSize(20F)
                document.add(Paragraph(titleDoc.text.toString()))

                document.setFontSize(12F)
                document.add(Paragraph(contentDoc.text.toString()))

                document.close()

                dialog.dismiss()
            }
            dialog.show()
        }

        createDoc.setOnClickListener {
            createDocDialog()
        }
    }
}