package com.n1ck120.easydoc.database.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

object SettingsDataStore {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    fun getDataStorePrefs(context: Context): DataStore<Preferences> {
        return context.dataStore
    }
}