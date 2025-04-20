package com.n1ck120.easydoc

import android.content.Context
import android.os.Environment
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.File
import java.io.FileOutputStream

object DocumentGen {
    fun veryEntry(entry : String) : String{
        if (entry == ""){
            return "Example"
        }else{
            return entry
        }
    }

    fun generateDocx(docTitle : String, docContent : String, docWorker : String, docName : String, context: Context){
        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl")
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl")
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl")

        val document = XWPFDocument()
        val tmpParagraph = document.createParagraph()
        val tmpRun = tmpParagraph.createRun()

        tmpRun.setText(docContent)
        //tmpRun.setFontSize(18)
        document.write(FileOutputStream(File(docName)))
        document.close()
        Toast.makeText(context, "Salvo em: $docName", Toast.LENGTH_LONG).show()
    }

    fun generatePDF(docTitle : String, docContent : String, docWorker : String, docName : String, context: Context){
        val writer = PdfWriter(File(docName))
        val pdfDoc = PdfDocument(writer)
        val document = Document(pdfDoc)

        //document.setFontSize(20F)
        document.add(Paragraph(docContent))
        //document.setFontSize(12F)
        //document.add(Paragraph(contentDoc))
        document.close()
        Toast.makeText(context, "Salvo em: $docName", Toast.LENGTH_LONG).show()
    }

    fun generateDoc(docTitle : String, docContent : String, docWorker : String, docName : String, docType : Int, context: Context){
        val dialogView = LayoutInflater.from(context).inflate(R.layout.rename_dialog, null)
        val dialog = MaterialAlertDialogBuilder(context)
            .setView(dialogView)
            .create()
        val nameText = dialogView.findViewById<TextInputEditText>(R.id.nameDoc)
        val overwriteBtn = dialogView.findViewById<Button>(R.id.overwrite)
        val renameBtn = dialogView.findViewById<Button>(R.id.rename)
        val dialogText = dialogView.findViewById<TextView>(R.id.dialogTitle)
        var docName2 : String
        if (docType == R.id.radioPdf){
            dialogText.text = "⚠ Esse pdf já existe ⚠"
            docName2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/"+veryEntry(docName)+".pdf"
            generatePDF(docTitle, docContent, docWorker, docName2, context)
            nameText.setText(veryEntry(docName))
        }else{
            dialogText.text = "⚠ Esse docx já existe ⚠"
            docName2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/"+veryEntry(docName)+".docx"
            generateDocx(docTitle, docContent, docWorker, docName2, context)
            nameText.setText(veryEntry(docName))
        }
        if(File(docName2).exists()){
            overwriteBtn.setOnClickListener {
                dialog.dismiss()
                Toast.makeText(context, "Salvo em: $docName2", Toast.LENGTH_LONG).show()
            }
            renameBtn.setOnClickListener {
                if (nameText.text.isNullOrBlank()){
                    nameText.error = "O nome não pode ser vazio"
                }else{
                    if (docType == R.id.radioPdf){
                        val a = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/"+nameText.text+".pdf"
                        generatePDF(docTitle, docContent, docWorker, a, context)
                    }else{
                        val a = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/"+nameText.text+".docx"
                        generateDocx(docTitle, docContent, docWorker, a, context)
                    }
                    Toast.makeText(context, "Salvo em: $docName2", Toast.LENGTH_LONG).show()
                }
            }
            dialog.show()
        }else{
            Toast.makeText(context, "Salvo em: $docName2", Toast.LENGTH_LONG).show()
        }
    }
}