package com.n1ck120.easydoc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup.MarginLayoutParams
import android.widget.Button
import android.widget.RadioGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private val doc = DocumentGen

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
        val fragmentos = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
        val fragContView = findViewById<FragmentContainerView>(R.id.fragmentContainerView)
        val vtoNavigation = bottomNavigation.viewTreeObserver


        vtoNavigation.addOnGlobalLayoutListener {
            val fragMargin = fragContView.layoutParams as MarginLayoutParams
            fragMargin.bottomMargin = bottomNavigation.height
        }

        createDoc.setOnClickListener {
            val dialogView = LayoutInflater.from(this).inflate(R.layout.create_doc, null)
            val dialog = MaterialAlertDialogBuilder(this)
                .setView(dialogView)
                .create()
            val titleDoc = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.title)
            val contentDoc = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.content)
            val workerDoc = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.worker)
            val outputDoc = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.outputname)
            val typeDoc = dialogView.findViewById<RadioGroup>(R.id.groupType)
            val generateBtn = dialogView.findViewById<Button>(R.id.generatedoc)

            generateBtn.setOnClickListener {
                doc.generateDoc(
                    titleDoc.text.toString(),
                    contentDoc.text.toString(),
                    workerDoc.text.toString(),
                    outputDoc.text.toString(), typeDoc.checkedRadioButtonId, this)
                dialog.dismiss()
            }
            dialog.show()
        }

        bottomNavigation.setOnItemSelectedListener { item ->
            val homeBtn = bottomNavigation.menu.findItem(R.id.item_1)
            val docsBtn = bottomNavigation.menu.findItem(R.id.item_2)
            val accountBtn = bottomNavigation.menu.findItem(R.id.item_3)
            val settingsBtn = bottomNavigation.menu.findItem(R.id.item_4)
            homeBtn.setIcon(R.drawable.outline_home_24)
            docsBtn.setIcon(R.drawable.outline_insert_drive_file_24)
            accountBtn.setIcon(R.drawable.outline_account_circle_24)
            settingsBtn.setIcon(R.drawable.outline_settings_24)

            when(item.itemId) {

                R.id.item_1 -> {
                    supportFragmentManager.commit {
                        setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        if (actualFragment(supportFragmentManager.findFragmentByTag("Home"))){
                            replace(R.id.fragmentContainerView, HomeFragment(), "Home")
                        }
                    }
                    item.setIcon(R.drawable.baseline_home_24)
                    true
                }
                R.id.item_2 -> {
                    supportFragmentManager.commit {
                        setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        if (actualFragment(supportFragmentManager.findFragmentByTag("Docs"))){
                            replace(R.id.fragmentContainerView, DocsFragment(), "Docs")
                        }
                    }
                    item.setIcon(R.drawable.baseline_insert_drive_file_24)
                    true
                }
                R.id.item_3 -> {
                    supportFragmentManager.commit {
                        setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        if (actualFragment(supportFragmentManager.findFragmentByTag("Account"))){
                            replace(R.id.fragmentContainerView, AccountFragment(), "Account")
                        }
                    }
                    item.setIcon(R.drawable.baseline_account_circle_24)
                    true
                }
                R.id.item_4 -> {
                    supportFragmentManager.commit {
                        setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        if (actualFragment(supportFragmentManager.findFragmentByTag("Settings"))){
                            replace(R.id.fragmentContainerView, SettingsFragment(), "Settings")
                        }
                    }
                    item.setIcon(R.drawable.baseline_settings_24)
                    true
                }
                else -> false
            }
        }
    }

    private fun actualFragment(tag : Fragment?): Boolean {
        if (tag != null && tag.isVisible){
            return false
        }else{
            return true
        }
    }
}