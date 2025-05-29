package com.n1ck120.easydoc

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var db : AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        //val fragContView = findViewById<FragmentContainerView>(R.id.fragmentContainerView)
        //val vtoNavigation = bottomNavigation.viewTreeObserver

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database.db"
        ).build()

        /*vtoNavigation.addOnGlobalLayoutListener {
            val fragMargin = fragContView.layoutParams as MarginLayoutParams
            fragMargin.bottomMargin = bottomNavigation.height
        }*/

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
        //Verifica o tema salvo no datastore e troca caso necessario
        val dataStore = SettingsDataStore.getDataStorePrefs(this)
        val key = intPreferencesKey("theme")
        lifecycleScope.launch {
            AppCompatDelegate.setDefaultNightMode(dataStore.data.first()[key] ?: MODE_NIGHT_FOLLOW_SYSTEM)
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