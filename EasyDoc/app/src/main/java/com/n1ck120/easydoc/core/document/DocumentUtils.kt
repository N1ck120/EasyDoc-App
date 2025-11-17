package com.n1ck120.easydoc.core.document

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.util.Log.ERROR
import android.widget.Toast
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfReader
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.n1ck120.easydoc.R
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.InputStream

object DocumentUtils {

    fun docClean(outuri: Uri, format: String?, context: Context): MutableList<String?>? {
        val resolver = context.contentResolver
        if (format == "application/pdf"){
            try {
                resolver.openOutputStream(outuri).use { outputStream ->
                    val document = PdfDocument(PdfWriter(outputStream))
                    document.documentInfo.creator?.let { document.documentInfo.creator = null}
                    document.documentInfo.title?.let { document.documentInfo.creator = null }
                    document.documentInfo.author?.let { document.documentInfo.creator = null }
                    document.documentInfo.keywords?.let { document.documentInfo.creator = null }
                    //document.documentInfo.producer = null
                    document.documentInfo.removeCreationDate()
                    document.documentInfo.setMoreInfo("ModDate", null)

                    document.close()
                }
                Toast.makeText(context,context.getString(R.string.metadata_removed), Toast.LENGTH_SHORT).show()
                resolver.openInputStream(outuri)?.use { stream ->
                    return docInfo(stream, format)
                }
            }catch (e: Exception){
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
            }
        }else{
            try {
                resolver.openInputStream(outuri).use { outputStream ->
                    val document = XWPFDocument(outputStream)
                    document.properties.coreProperties.modified?.let { document.properties.coreProperties.modified.time = 0 }
                    document.properties.coreProperties.creator?.let { document.properties.coreProperties.creator = null }
                    document.properties.coreProperties.lastModifiedByUser?.let { document.properties.coreProperties.lastModifiedByUser = null }
                    document.properties.coreProperties.created.time = 0
                    document.properties.extendedProperties.appVersion?.let { document.properties.extendedProperties.appVersion = null }
                    document.properties.extendedProperties.application?.let { document.properties.extendedProperties.application = null }
                    document.properties.extendedProperties.company?.let { document.properties.extendedProperties.company = null }
                    document.properties.extendedProperties.manager?.let { document.properties.extendedProperties.manager = null }
                    document.properties.extendedProperties.totalTime = 0

                    document.write(resolver.openOutputStream(outuri))
                }
                Toast.makeText(context,context.getString(R.string.metadata_removed), Toast.LENGTH_SHORT).show()

                resolver.openInputStream(outuri)?.use { stream ->
                    return docInfo(stream, format)
                }
            }catch (e: Exception){
                Log.println(ERROR, null, e.toString())
                //Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        return null
    }

    fun docInfo(doc: InputStream, format: String?): MutableList<String?> {
        val infoArray = mutableListOf<String?>()
        if (format == "application/pdf"){
            val document = PdfDocument(PdfReader(doc))
            document.documentInfo.creator?.let {infoArray.add("Criador: "+it)}
            document.documentInfo.title?.let {infoArray.add("Titulo: "+it)}
            document.documentInfo.author?.let {infoArray.add("Autor: "+it)}
            document.documentInfo.keywords?.let {infoArray.add("Keywords: "+it)}
            document.documentInfo.producer?.let {infoArray.add("Gerado por: "+it)}
            document.documentInfo.getMoreInfo("CreationDate")?.let {infoArray.add("Data de criação: "+it)}
            document.documentInfo.getMoreInfo("ModDate")?.let {infoArray.add("Última alteração: "+it)}
        }else{
            val document = XWPFDocument(doc)
            document.properties.coreProperties.creator?.let {infoArray.add("Gerado por: "+it)}
            document.properties.coreProperties.lastModifiedByUser?.let {infoArray.add("Última modificação feita por: "+it)}
            infoArray.add("Data de criação: "+document.properties.coreProperties.created.toString())
            document.properties.coreProperties.modified?.let {infoArray.add("Última alteração: "+it)}
            document.properties.extendedProperties.appVersion?.let {infoArray.add("Versão do app: "+it)}
            document.properties.extendedProperties.application?.let {infoArray.add("Aplicação: "+it)}
            document.properties.extendedProperties.company?.let {infoArray.add("Compania: "+it)}
            document.properties.extendedProperties.manager?.let {infoArray.add("Gerente: "+it)}
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