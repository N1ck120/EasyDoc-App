package com.n1ck120.easydoc.utils

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.n1ck120.easydoc.core.document.DocumentGen
import com.n1ck120.easydoc.R
import com.n1ck120.easydoc.database.room.Doc
import java.time.LocalDateTime

class DialogBuilder(private val context: Context, private val callback1: ((Doc) -> Unit)? = null, private val callback2: ((Boolean) -> Unit)? = null, private val callback3: ((Doc) -> Unit)? = null){

    fun docDialog(
        title: String,
        docTitle: String = "",
        docContent: String = "",
        docOut: String = "",
    ){
        val dialogView = LayoutInflater.from(context).inflate(R.layout.create_doc, null)
        val dialog = MaterialAlertDialogBuilder(context)
            .setView(dialogView)
            .create()

        val titleDialog = dialogView.findViewById<TextView>(R.id.textView)
        val titleDoc = dialogView.findViewById<TextInputEditText>(R.id.title)
        val contentDoc = dialogView.findViewById<TextInputEditText>(R.id.content)
        val outputDoc = dialogView.findViewById<TextInputEditText>(R.id.outputname)
        val typeDoc = dialogView.findViewById<RadioGroup>(R.id.groupType)
        val generateBtn = dialogView.findViewById<Button>(R.id.generatedoc)
        val save = dialogView.findViewById<Button>(R.id.save)
        val share = dialogView.findViewById<Button>(R.id.share)

        titleDialog.text = title
        titleDoc.setText(docTitle)
        contentDoc.setText(docContent)
        outputDoc.setText(docOut)

        val doc = DocumentGen

        fun getData(): String{
            var data : String
            if (LocalDateTime.now().dayOfMonth < 10){
                data = "0" + LocalDateTime.now().dayOfMonth.toString()
            }else{
                data = LocalDateTime.now().dayOfMonth.toString()
            }
            if (LocalDateTime.now().monthValue < 10){
                data = data + "/0" + LocalDateTime.now().monthValue.toString() + "/" + LocalDateTime.now().year.toString()
            }else{
                data = data + "/" + LocalDateTime.now().monthValue.toString() + "/" + LocalDateTime.now().year.toString()
            }
            if (LocalDateTime.now().hour < 10){
                data = data + " às 0" + LocalDateTime.now().hour.toString()
            }else{
                data = data + " às " + LocalDateTime.now().hour.toString()
            }
            if (LocalDateTime.now().minute < 10){
                data = data + ":0" + LocalDateTime.now().minute.toString()
            }else{
                data = data + ":" + LocalDateTime.now().minute.toString()
            }
            return data
        }

        save.setOnClickListener {
            if (titleDoc.text.isNullOrBlank()){
                titleDoc.error = "Campo obrigatório"
            }else{
                if (contentDoc.text.isNullOrBlank()){
                    contentDoc.error = "Campo obrigatório"
                }else{
                    val dox = Doc(
                        doc_name = outputDoc.text.toString(),
                        title = titleDoc.text.toString(),
                        content = contentDoc.text.toString(),
                        date = getData()
                    )
                    callback1?.invoke(dox)
                    dialog.dismiss()
                }
            }
        }

        generateBtn.setOnClickListener {
            if (outputDoc.text.toString().contains(Regex("^[A-Za-z0-9._%+-]"))){
                doc.docGenerator(
                    titleDoc.text.toString(),
                    contentDoc.text.toString(),
                    outputDoc.text.toString(),
                    dialogView.resources.getResourceEntryName(typeDoc.checkedRadioButtonId),//Busca o nome do ID da opção selecionada
                    context)
                val dox = Doc(
                    doc_name = outputDoc.text.toString(),
                    title = titleDoc.text.toString(),
                    content = contentDoc.text.toString(),
                    date = getData()
                )
                callback3?.invoke(dox)
                dialog.dismiss()
            }else{
                outputDoc.error = "O nome contém caracteres inválidos!"
            }
        }

        share.setOnClickListener {
            if (outputDoc.text.toString().contains(Regex("^[A-Za-z0-9._%+-]"))){
                val uri = doc.docGenerator(
                    titleDoc.text.toString(),
                    contentDoc.text.toString(),
                    outputDoc.text.toString(),
                    dialogView.resources.getResourceEntryName(typeDoc.checkedRadioButtonId),//Busca o nome do ID da opção selecionada
                    context)

                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "application/pdf"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }

                // Iniciar o chooser
                context.startActivity(Intent.createChooser(shareIntent, "Compartilhar via"))

                dialog.dismiss()
            }else{
                outputDoc.error = "O nome contém caracteres inválidos!"
            }
        }
        dialog.show()
    }

    fun genericDialog(title: String, message: String?, context: Context, confirm: String? = "Confirmar", cancel: String? = "Cancelar", cancelable: Boolean = true) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(cancelable)
            .setNegativeButton(cancel) { dialog, which ->
                dialog.dismiss()
                callback2?.invoke(false)
            }
            .setPositiveButton(confirm) { dialog, which ->
                dialog.dismiss()
                callback2?.invoke(true)
            }
            .show()
    }
}