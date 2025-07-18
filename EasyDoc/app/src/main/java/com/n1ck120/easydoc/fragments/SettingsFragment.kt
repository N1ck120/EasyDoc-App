package com.n1ck120.easydoc.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.fragment.app.Fragment
import androidx.core.net.toUri
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.materialswitch.MaterialSwitch
import com.n1ck120.easydoc.activities.MainActivity
import com.n1ck120.easydoc.R
import com.n1ck120.easydoc.database.datastore.SettingsDataStore
import com.n1ck120.easydoc.utils.DialogBuilder
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bottomNav : BottomNavigationView = (requireActivity() as MainActivity).bottomNavigation
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        val gitCard = view.findViewById<MaterialCardView>(R.id.githubCard)
        val theme = view.findViewById<Button>(R.id.btnTheme)
        val offlineSwitch = view.findViewById<MaterialSwitch>(R.id.offlineMode)
        val saveSwitch = view.findViewById<MaterialSwitch>(R.id.saveExported)
        val homeBtn = bottomNav.menu.findItem(R.id.item_1)
        val accountBtn = bottomNav.menu.findItem(R.id.item_3)
        val settingBtn = bottomNav.menu.findItem(R.id.item_4)
        val helpBtn = view.findViewById<Button>(R.id.helpButton)

        val dialog = DialogBuilder(requireContext(), {},{},{})

        homeBtn.setIcon(R.drawable.outline_insert_drive_file_24)
        settingBtn.setIcon(R.drawable.baseline_settings_24)

        gitCard.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, "https://github.com/N1ck120/EasyDoc-App".toUri())
            startActivity(browserIntent)
        }

        val dataStore = SettingsDataStore.getDataStorePrefs(requireContext())
        val key = intPreferencesKey("theme")
        val offlineMode = intPreferencesKey("offlineMode")
        val saveExported = intPreferencesKey("saveExported")

        lifecycleScope.launch {
            if (runBlocking { dataStore.data.first()[saveExported] ?: 1 } == 0){
                saveSwitch.isChecked = false
            }
        }

        lifecycleScope.launch {
            if (runBlocking { dataStore.data.first()[offlineMode] ?: 0 } == 1){
                offlineSwitch.isChecked = true
            }
        }

        offlineSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                lifecycleScope.launch {
                    dataStore.edit { settings ->
                        settings[offlineMode] = 1
                        accountBtn.isVisible = false
                    }
                }
            }else{
                lifecycleScope.launch {
                    dataStore.edit { settings ->
                        settings[offlineMode] = 0
                        accountBtn.isVisible = true
                    }
                }
            }
        }

        saveSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                lifecycleScope.launch {
                    dataStore.edit { settings ->
                        settings[saveExported] = 1
                    }
                }
            }else{
                lifecycleScope.launch {
                    dataStore.edit { settings ->
                        settings[saveExported] = 0
                    }
                }
            }
        }

        when(AppCompatDelegate.getDefaultNightMode()){
            MODE_NIGHT_FOLLOW_SYSTEM ->{
                theme.text = "Sistema"
            }
            MODE_NIGHT_NO ->{
                theme.text = "Claro"
            }
            MODE_NIGHT_YES ->{
                theme.text = "Escuro"
            }
            else -> theme.text = "Sistema"
        }

        theme.setOnClickListener {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.theme_dialog, null)
            val dialog = MaterialAlertDialogBuilder(dialogView.context)
                .setView(dialogView)
                .create()
            val btnOk = dialogView.findViewById<Button>(R.id.ok)
            val btnCancel = dialogView.findViewById<Button>(R.id.cancel)
            val grpTheme = dialogView.findViewById<RadioGroup>(R.id.groupTheme)
            val radioSys = dialogView.findViewById<RadioButton>(R.id.radioSystem)
            val radioLht = dialogView.findViewById<RadioButton>(R.id.radioLight)
            val radioDrk = dialogView.findViewById<RadioButton>(R.id.radioDark)

            when(AppCompatDelegate.getDefaultNightMode()){
                MODE_NIGHT_FOLLOW_SYSTEM ->{
                    radioSys.isChecked = true
                }
                MODE_NIGHT_NO ->{
                    radioLht.isChecked = true
                }
                MODE_NIGHT_YES ->{
                    radioDrk.isChecked = true
                }
                else -> radioSys.isChecked = true
            }

            btnOk.setOnClickListener {
                when(grpTheme.checkedRadioButtonId){
                    R.id.radioSystem ->{
                        theme.text = "Sistema"
                        val a = lifecycleScope.launch {
                            dataStore.edit { settings ->
                                settings[key] = MODE_NIGHT_FOLLOW_SYSTEM
                            }
                        }
                        a.invokeOnCompletion {
                            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
                        }
                    }
                    R.id.radioLight ->{
                        theme.text = "Claro"
                        val a = lifecycleScope.launch {
                            dataStore.edit { settings ->
                                settings[key] = MODE_NIGHT_NO
                            }
                        }
                        a.invokeOnCompletion {
                            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
                        }
                    }
                    R.id.radioDark ->{
                        theme.text = "Escuro"
                        val a = lifecycleScope.launch {
                            dataStore.edit { settings ->
                                settings[key] = MODE_NIGHT_YES
                            }
                        }
                        a.invokeOnCompletion {
                            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
                        }
                    }
                }
                dialog.dismiss()
            }
            btnCancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

        helpBtn.setOnClickListener {
            dialog.genericDialog("Ajuda","Tema do app: Define o esquema de cores que será usado por todo o aplicativo. \n\n Manter logado: Mantém sua sessão ativa removendo a necessidade de fazer login a cada vez que abre o app(esta opção só é habilitada quando logado). \n\n Sincronização automática: Baixa e Atualiza os documentos salvos na nuvem automaticamente. \n\n Modo offline: Desabilita todas as funcionalidades que dependem de internet e loga automaticamente no modo offline. \n\n Salvar ao exportar: Ao exportar um documento o mesmo será salvo na tela inicial.",requireContext(),"Entendi", null)
        }
        return view
    }
}