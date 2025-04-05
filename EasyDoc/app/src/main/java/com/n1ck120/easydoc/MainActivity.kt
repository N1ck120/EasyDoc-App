package com.n1ck120.easydoc

import android.app.AlertDialog
import android.content.ClipData.Item
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.component1
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import androidx.fragment.app.commit
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.itextpdf.layout.element.Paragraph
import java.io.File

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
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        createDoc.setOnClickListener {
            createDocDialog()
        }

        bottomNavigation.setOnItemSelectedListener { item ->
            val homeBtn = bottomNavigation.menu.findItem(R.id.item_1)
            val docsBtn = bottomNavigation.menu.findItem(R.id.item_2)
            val accountBtn = bottomNavigation.menu.findItem(R.id.item_3)
            val aettingsBtn = bottomNavigation.menu.findItem(R.id.item_4)
            homeBtn.setIcon(R.drawable.round_home_24)
            docsBtn.setIcon(R.drawable.round_insert_drive_file_24)
            accountBtn.setIcon(R.drawable.round_account_circle_24)
            aettingsBtn.setIcon(R.drawable.round_settings_24)

            when(item.itemId) {
                R.id.item_1 -> {
                    supportFragmentManager.commit {
                        setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        replace(R.id.fragmentContainerView, HomeFragment())
                    }

                    item.setIcon(R.drawable.outline_home_24)
                    // Respond to navigation item 1 click
                    true

                }
                R.id.item_2 -> {
                    supportFragmentManager.commit {
                        setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        replace(R.id.fragmentContainerView, DocsFragment())
                    }

                    item.setIcon(R.drawable.outline_insert_drive_file_24)
                    // Respond to navigation item 2 click
                    true
                }
                R.id.item_3 -> {
                    supportFragmentManager.commit {
                        setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        replace(R.id.fragmentContainerView, AccountFragment())
                    }
                    item.setIcon(R.drawable.outline_account_circle_24)
                    // Respond to navigation item 2 click
                    true
                }
                R.id.item_4 -> {
                    supportFragmentManager.commit {
                        setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        replace(R.id.fragmentContainerView, SettingsFragment())
                    }
                    item.setIcon(R.drawable.outline_settings_24)
                    // Respond to navigation item 2 click
                    true
                }
                else -> false
            }
        }
    }

    private fun contentVer(a: TextInputEditText, b: Boolean = false) : String{
        if (a.text.isNullOrBlank() && !b){
            return ""
        } else if (a.text.isNullOrBlank() && b){
            return "Exemplo"
        } else{
            return  a.text.toString()
        }
    }

    private fun createDocDialog(){
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
            val pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/"+contentVer(outputDoc, true)+".pdf"
            Toast.makeText(this, "Salvo em: $pdfPath", Toast.LENGTH_LONG).show()
            val file = File(pdfPath)


            val writer = PdfWriter(file)
            val pdfDoc = PdfDocument(writer)
            val document = Document(pdfDoc)

            document.setFontSize(20F)
            document.add(Paragraph(contentVer(titleDoc)))

            document.setFontSize(12F)
            document.add(Paragraph(contentVer(contentDoc)))

            document.setFontSize(12F)
            document.add(Paragraph(contentVer(workerDoc)))

            document.close()
            dialog.dismiss()
        }
        dialog.show()
    }
}