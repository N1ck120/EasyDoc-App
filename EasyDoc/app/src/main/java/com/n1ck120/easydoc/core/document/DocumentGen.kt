package com.n1ck120.easydoc.core.document

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfDocumentInfo
import com.itextpdf.kernel.pdf.PdfReader
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.InputStream

object DocumentGen {

    fun docClean(doc: InputStream, format: String?){
        if (format == "application/pdf"){
            val document = PdfDocument(PdfReader(doc))
            document.documentInfo.creator
            document.documentInfo.title
            document.documentInfo.author
            document.documentInfo.keywords
            document.documentInfo.producer
            document.documentInfo.getMoreInfo("CreationDate")
            document.documentInfo.getMoreInfo("ModDate")
        }else{
            val document = XWPFDocument(doc)
            document.properties.coreProperties.creator
            document.properties.coreProperties.lastModifiedByUser
            document.properties.coreProperties.created
            document.properties.extendedProperties.appVersion
            document.properties.extendedProperties.application
            document.properties.extendedProperties.company
            document.properties.extendedProperties.manager
            document.properties.extendedProperties.totalTime
        }
    }

    fun docInfo(doc: InputStream, format: String?): MutableList<String?> {
        val infoArray = mutableListOf<String?>()
        if (format == "application/pdf"){
            val document = PdfDocument(PdfReader(doc))
            document.documentInfo.creator.takeIf { it != null }?.let {infoArray.add("Criador: "+it)}
            document.documentInfo.title.takeIf { it != null }?.let {infoArray.add("Titulo: "+it)}
            document.documentInfo.author.takeIf { it != null }?.let {infoArray.add("Autor: "+it)}
            document.documentInfo.keywords.takeIf { it != null }?.let {infoArray.add("Keywords: "+it)}
            document.documentInfo.producer.takeIf { it != null }?.let {infoArray.add("Gerado por: "+it)}
            document.documentInfo.getMoreInfo("CreationDate").takeIf { it != null }?.let {infoArray.add("Data de criação: "+it)}
            document.documentInfo.getMoreInfo("ModDate").takeIf { it != null }?.let {infoArray.add("Última alteração: "+it)}
        }else{
            val document = XWPFDocument(doc)
            document.properties.coreProperties.creator.takeIf { it != null }?.let {infoArray.add("Gerado por: "+it)}
            document.properties.coreProperties.lastModifiedByUser.takeIf { it != null }?.let {infoArray.add("Última modificação feita por: "+it)}
            infoArray.add("Data de criação: "+document.properties.coreProperties.created.toString())
            document.properties.extendedProperties.appVersion.takeIf { it != null }?.let {infoArray.add("Versão do app: "+it)}
            document.properties.extendedProperties.application.takeIf { it != null }?.let {infoArray.add("Aplicação: "+it)}
            document.properties.extendedProperties.company.takeIf { it != null }?.let {infoArray.add("Compania: "+it)}
            document.properties.extendedProperties.manager.takeIf { it != null }?.let {infoArray.add("Gerente: "+it)}
            infoArray.add("Tempo total de edição: "+document.properties.extendedProperties.totalTime.toString())
        }
        return infoArray
    }

    fun docGenerator(docTitle : String, docContent : String, docName : String, format : String, context: Context): Uri? {
        val resolver = context.contentResolver
        val collection = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

        val metaValues = ContentValues().apply {
            put(MediaStore.Files.FileColumns.DISPLAY_NAME, "$docName.$format")
            if (format == "pdf"){
                put(MediaStore.Files.FileColumns.MIME_TYPE, "application/pdf")
            }else{
                put(MediaStore.Files.FileColumns.MIME_TYPE, "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
            }
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
                    //System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl")
                    //System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl")
                    //System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl")

                    val document = XWPFDocument()
                    val tmpParagraph = document.createParagraph()
                    val tmpParagraph2 = document.createParagraph()

                    //tmpParagraph.alignment = ParagraphAlignment.LEFT
                    val tmpRun = tmpParagraph.createRun()
                    val tmpRun2 = tmpParagraph2.createRun()

                    if (docTitle.contains("\n")){
                        val lines = docTitle.split("\n")
                        var i = 0
                        while (i < lines.size){
                            tmpRun.setText(lines[i])
                            tmpRun.addBreak()
                            i++
                        }
                    }else{
                        tmpRun.setText(docTitle)
                        tmpRun2.addBreak()
                    }

                    if (docContent.contains("\n")){
                        val lines2 = docContent.split("\n")
                        var i2 = 0
                        while (i2 < lines2.size){
                            tmpRun2.setText(lines2[i2])
                            tmpRun2.addBreak()
                            i2++
                        }
                    }else{
                        tmpRun2.setText(docContent)
                    }
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