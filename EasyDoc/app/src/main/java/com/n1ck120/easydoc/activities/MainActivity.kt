package com.n1ck120.easydoc.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.n1ck120.easydoc.R
import com.n1ck120.easydoc.database.datastore.SettingsDataStore
import com.n1ck120.easydoc.database.room.AppDatabase
import com.n1ck120.easydoc.fragments.AccountFragment
import com.n1ck120.easydoc.fragments.ToolsFragment
import com.n1ck120.easydoc.fragments.HomeFragment
import com.n1ck120.easydoc.fragments.SettingsFragment
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var db : AppDatabase
    lateinit var bottomNavigation : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database.db"
        ).build()

        val homeBtn = bottomNavigation.menu.findItem(R.id.item_1)
        val docsBtn = bottomNavigation.menu.findItem(R.id.item_2)
        val accountBtn = bottomNavigation.menu.findItem(R.id.item_3)
        val settingsBtn = bottomNavigation.menu.findItem(R.id.item_4)

        bottomNavigation.setOnItemSelectedListener { item ->
            homeBtn.setIcon(R.drawable.outline_insert_drive_file_24)
            docsBtn.setIcon(R.drawable.outline_build_24)
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
                    item.setIcon(R.drawable.baseline_insert_drive_file_24)
                    true
                }
                R.id.item_2 -> {
                    supportFragmentManager.commit {
                        setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        if (actualFragment(supportFragmentManager.findFragmentByTag("Docs"))){
                            replace(R.id.fragmentContainerView, ToolsFragment(), "Docs")
                        }
                    }
                    item.setIcon(R.drawable.baseline_build_24)
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
        val offlineMode = intPreferencesKey("offlineMode")
        lifecycleScope.launch {
            AppCompatDelegate.setDefaultNightMode(dataStore.data.first()[key] ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            accountBtn.isVisible = (dataStore.data.first()[offlineMode] ?: 0) != 1
        }
    }

    private fun actualFragment(tag : Fragment?): Boolean {
        return !(tag != null && tag.isVisible)
    }
}