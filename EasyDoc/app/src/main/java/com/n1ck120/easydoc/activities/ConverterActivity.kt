package com.n1ck120.easydoc.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfReader
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor
import com.n1ck120.easydoc.R
import com.n1ck120.easydoc.core.document.DocumentUtils
import com.n1ck120.easydoc.utils.DialogBuilder
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.apache.poi.xwpf.usermodel.XWPFParagraph

class ConverterActivity : AppCompatActivity() {
    val dialog = DialogBuilder(this, genericCallback = {
        confirm ->
        if (confirm){
            if (fileMime == "application/pdf"){
                doc.docGenerator(textpdf.lineSequence().first(),textpdf.replace(textpdf.lineSequence().first()+"\n", "")+"\n",filename1?.replace(".pdf", "") ?: "", "docx",this)
            }else{
                doc.docGenerator(textdocx.lineSequence().first(),textdocx.replace(textdocx.lineSequence().first()+"\n", "")+"\n",filename1?.replace(".pdf", "") ?: "", "pdf",this)
            }
        }
    })
    var filename1 : String? = null
    var fileMime : String? = null

    var textpdf = ""

    var textdocx = ""

    val doc = DocumentUtils

    /*try {

    }catch (e: Exception){
        Toast.makeText(this, "Arquivo inválido ou corrompido", Toast.LENGTH_SHORT).show()
    }*/
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
                    if (mime == "application/pdf"){
                        val pdfDoc = PdfDocument(PdfReader(inputStream))
                        val textoExtraido = PdfTextExtractor.getTextFromPage(pdfDoc.getPage(1))
                        textpdf = textoExtraido
                        dialog.genericDialog("Converter arquivo" ,"Você converterá $filename1 para DOCX, o arquivo original NÃO será apagado continuar?", this,"Confirmar", "Canacelar", false)
                        pdfDoc.close()
                    }else{
                        val document = XWPFDocument(stream)
                        val paragrafos: List<XWPFParagraph> = document.paragraphs
                        var texto = ""
                        paragrafos.forEach { paragrafo ->
                            texto = texto + paragrafo.text
                        }
                        textdocx = texto
                        dialog.genericDialog("Converter arquivo" ,"Você converterá $filename1 para PDF, o arquivo original NÃO será apagado continuar?", this,"Confirmar", "Canacelar", false)
                    }
                }
            }catch (e: Exception){
                Toast.makeText(this, getString(R.string.invalid_file), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_converter)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //Declaração de variaveis globais
        val backBtn = findViewById<MaterialButton>(R.id.backButton)
        val helpBtn = findViewById<MaterialButton>(R.id.helpButton)
        val fileSelector = findViewById<MaterialCardView>(R.id.selectFile)
        //Instanciando DialogBuilder


        fileSelector.setOnClickListener {
            pickFile.launch(arrayOf("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/pdf"))
        }

        backBtn.setOnClickListener {
            finish()
        }

        helpBtn.setOnClickListener {
            dialog.genericDialog(getString(R.string.help),
                getString(R.string.help_converter),this, getString(R.string.understood), null)
        }
    }
}