package com.n1ck120.easydoc

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.core.net.toUri
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.google.android.material.badge.BadgeDrawable.TOP_START
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SettingsFragment : Fragment() {

    lateinit var db : AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bottomNav : BottomNavigationView = (requireActivity() as MainActivity).bottomNavigation
        //val db = (requireActivity() as MainActivity).db
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        val gitCard = view.findViewById<MaterialCardView>(R.id.githubCard)
        val theme = view.findViewById<Button>(R.id.btnTheme)
        val offlineSwitch = view.findViewById<MaterialSwitch>(R.id.swwitch3)

        /*
        val badge = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).getOrCreateBadge(R.id.item_1)
        badge.badgeGravity = TOP_START
        badge.backgroundColor = ContextCompat.getColor(requireContext(), R.color.md_theme_primary)
        badge.badgeTextColor = ContextCompat.getColor(requireContext(), R.color.md_theme_primaryContainer)

        lifecycleScope.launch {
            db.userDao().getAll().collect { docs ->
                val dataset = mutableListOf<Doc>()
                val iterator = docs.listIterator()
                while (iterator.hasNext()){
                    dataset.add(iterator.next())
                    badge.number = dataset.size
                }
            }
        }
       */

        val homeBtn = bottomNav.menu.findItem(R.id.item_1)
        val accountBtn = bottomNav.menu.findItem(R.id.item_3)
        val settingBtn = bottomNav.menu.findItem(R.id.item_4)

        homeBtn.setIcon(R.drawable.outline_insert_drive_file_24)
        settingBtn.setIcon(R.drawable.baseline_settings_24)

        gitCard.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, "https://github.com/N1ck120/EasyDoc-App".toUri())
            startActivity(browserIntent)
        }

        val dataStore = SettingsDataStore.getDataStorePrefs(requireContext())
        val key = intPreferencesKey("theme")
        val offlineMode = intPreferencesKey("offlineMode")

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
        return view
    }
}