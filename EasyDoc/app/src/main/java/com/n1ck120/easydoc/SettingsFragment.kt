package com.n1ck120.easydoc

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.fragment.app.Fragment
import com.google.android.material.materialswitch.MaterialSwitch
import androidx.core.net.toUri
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        val github = view.findViewById<ImageButton>(R.id.github)
        val autoTheme = view.findViewById<MaterialSwitch>(R.id.switch1)
        val darkTheme = view.findViewById<MaterialSwitch>(R.id.switch2)
        val autoSync = view.findViewById<MaterialSwitch>(R.id.switch3)

        github.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, "https://github.com/N1ck120/EasyDoc-App".toUri())
            startActivity(browserIntent)
        }

        val dataStore = SettingsDataStore.getDataStorePrefs(requireContext())
        val key = intPreferencesKey("theme")
        val key1 = intPreferencesKey("theme2")

        //Restaura o ultimo e estado dos switches
        lifecycleScope.launch {
            AppCompatDelegate.setDefaultNightMode(dataStore.data.first()[key] ?: (dataStore.data.first()[key1] ?: MODE_NIGHT_NO))
            when(dataStore.data.first()[key]){
                MODE_NIGHT_FOLLOW_SYSTEM -> autoTheme.isChecked = true
                else -> {
                    when(dataStore.data.first()[key1] ?: MODE_NIGHT_NO){
                        MODE_NIGHT_YES -> {
                            autoTheme.isChecked = false
                            darkTheme.isChecked = true
                            darkTheme.isEnabled = true
                        }
                        MODE_NIGHT_NO ->{
                            autoTheme.isChecked = false
                            darkTheme.isChecked = false
                            darkTheme.isEnabled = true
                        }
                    }
                }
            }
        }

        //Verifica o estado do switch "Seguir tema do sistema"
        autoTheme.setOnCheckedChangeListener { _, isChecked ->
            darkTheme.isEnabled = !isChecked

            if (isChecked){
                darkTheme.isEnabled = false
                darkTheme.isChecked = false
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
                lifecycleScope.launch {
                    dataStore.edit { settings ->
                        settings[key] = MODE_NIGHT_FOLLOW_SYSTEM
                    }
                }
            }else{
                darkTheme.isEnabled = true
                lifecycleScope.launch {
                    dataStore.edit { settings ->
                        settings.remove(key)
                        settings[key1] = MODE_NIGHT_NO
                        AppCompatDelegate.setDefaultNightMode(dataStore.data.first()[key1] ?: MODE_NIGHT_NO)
                    }
                }
            }
        }

        //Verifica o estado do switch "Tema escuro"
        darkTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
                lifecycleScope.launch {
                    dataStore.edit { settings ->
                        settings[key1] = MODE_NIGHT_YES
                    }
                }
            }else{
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
                lifecycleScope.launch {
                    dataStore.edit { settings ->
                        settings[key1] = MODE_NIGHT_NO
                    }
                }
            }
        }

        return view
    }
}
