package com.n1ck120.easydoc.core.document

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import org.apache.poi.xwpf.usermodel.ParagraphAlignment
import org.apache.poi.xwpf.usermodel.XWPFDocument

object DocumentGen {

    fun docGenerator(docTitle : String, docContent : String, docName : String, format : String, context: Context): Uri? {
        val resolver = context.contentResolver
        val collection = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

        val metaValues = ContentValues().apply {
            put(MediaStore.Files.FileColumns.DISPLAY_NAME, "$docName.$format")
            put(MediaStore.Files.FileColumns.MIME_TYPE, "application/$format")
            put(MediaStore.Files.FileColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
            put(MediaStore.Files.FileColumns.IS_PENDING, 1)
        }

        val uri = resolver.insert(collection, metaValues)

        if (uri != null) {
            // Abre a stream de escrita
            resolver.openOutputStream(uri).use { outputStream ->
                if (format == "pdf"){
                    val writer = PdfWriter(outputStream)
                    val pdfDoc = PdfDocument(writer)
                    val document = Document(pdfDoc)

                    //document.setFontSize(20F)
                    document.add(Paragraph(docTitle))
                    document.add(Paragraph(docContent))
                    document.close()
                }else{
                    System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl")
                    System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl")
                    System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl")

                    val document = XWPFDocument()
                    val tmpParagraph = document.createParagraph()

                    tmpParagraph.alignment = ParagraphAlignment.LEFT
                    val tmpRun = tmpParagraph.createRun()

                    tmpRun.setText(docTitle)

                    tmpRun.setText(docContent)
                    //tmpRun.setFontSize(18)
                    document.write(outputStream)
                    document.close()
                }
            }
            metaValues.clear()
            metaValues.put(MediaStore.Files.FileColumns.IS_PENDING, 0)
            resolver.update(uri, metaValues, null, null)

            Toast.makeText(context, "$docName.$format salvo em documentos", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Erro ao criar o arquivo", Toast.LENGTH_SHORT).show()
        }
        return uri
    }
}