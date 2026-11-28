package com.example.myduitgw.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore(name = "user_session")

class SessionManager(private val context: Context) {

    companion object {
        private val TOKEN = stringPreferencesKey("token")
        private val USER_ID = intPreferencesKey("user_id")
        private val NAME = stringPreferencesKey("name")
        private val EMAIL = stringPreferencesKey("email")
    }

    suspend fun saveSession(
        token: String,
        userId: Int,
        name: String,
        email: String
    ) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN] = token
            preferences[USER_ID] = userId
            preferences[NAME] = name
            preferences[EMAIL] = email
        }
    }

    suspend fun getToken(): String? =
        context.dataStore.data.first()[TOKEN]

    suspend fun getName(): String? =
        context.dataStore.data.first()[NAME]

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }

    suspend fun hasSession(): Boolean {
        return !getToken().isNullOrBlank()
    }
}