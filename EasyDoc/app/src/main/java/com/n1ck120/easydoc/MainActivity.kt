package com.n1ck120.easydoc

import android.app.AlertDialog
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
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
        
        fun createDocDialog(){
            val dialogView = LayoutInflater.from(this).inflate(R.layout.create_doc, null)
            val dialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .create()
            val titleDoc = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.title)
            val contentDoc = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.content)
            val workerDoc = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.worker)
            val outputDoc = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.outputname)
            val generateBtn = dialogView.findViewById<Button>(R.id.generatedoc)

            generateBtn.setOnClickListener {
                // Define o caminho para salvar o PDF na pasta Downloads
                val pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/exemplo.pdf"
                val file = File(pdfPath)

                val writer = PdfWriter(file)
                val pdfDoc = PdfDocument(writer)
                val document = Document(pdfDoc)

                document.add(Paragraph("Olá, este é um exemplo simples de um PDF! Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."))

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