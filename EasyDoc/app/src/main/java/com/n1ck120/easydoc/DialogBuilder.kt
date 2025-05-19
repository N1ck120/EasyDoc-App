package com.n1ck120.easydoc

import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import java.time.LocalDateTime

class DialogBuilder(private val context: Context, private val callback1: (Doc) -> Unit, private val callback2: (Boolean) -> Unit){
    fun docDialog(
        title: String,
        docTitle: String = "",
        docContent: String = "",
        docWorker: String = "",
        docOut: String = "",
    ){
        val dialogView = LayoutInflater.from(context).inflate(R.layout.create_doc, null)
        val dialog = MaterialAlertDialogBuilder(context)
            .setView(dialogView)
            .create()

        val titleDialog = dialogView.findViewById<TextView>(R.id.textView)
        val titleDoc = dialogView.findViewById<TextInputEditText>(R.id.title)
        val contentDoc = dialogView.findViewById<TextInputEditText>(R.id.content)
        val workerDoc = dialogView.findViewById<TextInputEditText>(R.id.worker)
        val outputDoc = dialogView.findViewById<TextInputEditText>(R.id.outputname)
        val typeDoc = dialogView.findViewById<RadioGroup>(R.id.groupType)
        val generateBtn = dialogView.findViewById<Button>(R.id.generatedoc)
        val save = dialogView.findViewById<Button>(R.id.save)

        titleDialog.text = title
        titleDoc.setText(docTitle)
        contentDoc.setText(docContent)
        workerDoc.setText(docWorker)
        outputDoc.setText(docOut)

        val doc = DocumentGen

        save.setOnClickListener {
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
                        date = data
                    )
                    callback1(dox)
                    dialog.dismiss()
                }
            }

        }

        generateBtn.setOnClickListener {
            doc.generateDoc(
                titleDoc.text.toString(),
                contentDoc.text.toString(),
                workerDoc.text.toString(),
                outputDoc.text.toString(), typeDoc.checkedRadioButtonId, context)
            dialog.dismiss()
        }
        dialog.show()
    }

    fun genericDialog(title: String, context: Context, confirm: String = "Confirmar", cancel: String = "Cancelar") {
        val dialogView = LayoutInflater.from(this.context).inflate(R.layout.generic_dialog, null)
        val dialog = MaterialAlertDialogBuilder(dialogView.context)
            .setView(dialogView)
            .create()
        val message = dialogView.findViewById<TextView>(R.id.warning)
        val confirmBtn = dialogView.findViewById<Button>(R.id.confirm)
        val cancelBtn = dialogView.findViewById<Button>(R.id.cancel)

        message.text = title
        confirmBtn.text = confirm.toString()
        cancelBtn.text = cancel.toString()

        confirmBtn.setOnClickListener{
            dialog.dismiss()
            callback2(true)
        }
        cancelBtn.setOnClickListener{
            dialog.dismiss()
            callback2(false)
        }
        dialog.show()
    }
}