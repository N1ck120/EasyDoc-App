package com.n1ck120.easydoc

import android.content.Context
import android.os.Environment
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import java.io.File

object DocumentGen {
    fun veryEntry(entry : String) : String{
        if (entry == ""){
            return "Example"
        }else{
            return entry
        }
    }

    fun generateDoc(titleDoc : String = "", contentDoc : String = "", workerDoc : String = "", nameDoc : String, context: Context){
        val pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/"+veryEntry(nameDoc)+".pdf"
        var file = File(pdfPath)
        if(file.exists()){
            val dialogView = LayoutInflater.from(context).inflate(R.layout.rename_dialog, null)
            val dialog = MaterialAlertDialogBuilder(context)
                .setView(dialogView)
                .create()
            val nameText = dialogView.findViewById<TextInputEditText>(R.id.nameDoc)
            val overwriteBtn = dialogView.findViewById<Button>(R.id.overwrite)
            val renameBtn = dialogView.findViewById<Button>(R.id.rename)
            nameText.setText(veryEntry(nameDoc))

            overwriteBtn.setOnClickListener {
                dialog.dismiss()
                val writer = PdfWriter(file)
                val pdfDoc = PdfDocument(writer)
                val document = Document(pdfDoc)

                document.setFontSize(20F)
                document.add(Paragraph(titleDoc))

                document.setFontSize(12F)
                document.add(Paragraph(contentDoc))

                document.setFontSize(12F)
                document.add(Paragraph(workerDoc))

                document.close()
                Toast.makeText(context, "Salvo em: $pdfPath", Toast.LENGTH_LONG).show()
            }
            renameBtn.setOnClickListener {
                if (nameText.text.isNullOrBlank()){
                    nameText.error = "O nome não pode ser vazio"
                }else{
                    file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/"+nameText.text+".pdf")
                    dialog.dismiss()
                    val writer = PdfWriter(file)
                    val pdfDoc = PdfDocument(writer)
                    val document = Document(pdfDoc)

                    document.setFontSize(20F)
                    document.add(Paragraph(titleDoc))

                    document.setFontSize(12F)
                    document.add(Paragraph(contentDoc))

                    document.setFontSize(12F)
                    document.add(Paragraph(workerDoc))

                    document.close()
                    Toast.makeText(context, "Salvo em: $file", Toast.LENGTH_LONG).show()
                }
            }
            dialog.show()
        }else{
            val writer = PdfWriter(file)
            val pdfDoc = PdfDocument(writer)
            val document = Document(pdfDoc)

            document.setFontSize(20F)
            document.add(Paragraph(titleDoc))

            document.setFontSize(12F)
            document.add(Paragraph(contentDoc))

            document.setFontSize(12F)
            document.add(Paragraph(workerDoc))

            document.close()
            Toast.makeText(context, "Salvo em: $pdfPath", Toast.LENGTH_LONG).show()
        }
    }
}