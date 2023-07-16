package com.example.where.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "local")

class LocalDataSource(private val context: Context) {
    companion object {
        val ACCESS = stringPreferencesKey("access")
        val REFRESH = stringPreferencesKey("refresh")
    }

    suspend fun saveToken(
        access: String,
        refresh: String
    ) {
        context.dataStore.edit {
            it[ACCESS] = access
            it[REFRESH] = refresh
        }
    }

    fun getAccess() = context.dataStore.data.map { it[ACCESS] ?: "" }

    fun getRefresh() = context.dataStore.data.map { it[REFRESH] ?: "" }
}