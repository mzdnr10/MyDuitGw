
package com.example.myduitgw.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(
    name = "user_session"
)

class SessionManager(context: Context) {

    private val dataStore = context.applicationContext.dataStore

    companion object {
        private val TOKEN = stringPreferencesKey("sanctum_token")
        private val USER_ID = stringPreferencesKey("user_id")
        private val USER_NAME = stringPreferencesKey("user_name")
        private val USER_EMAIL = stringPreferencesKey("user_email")
    }

    val token: Flow<String?> = dataStore.data.map {
        it[TOKEN]
    }

    suspend fun saveSession(
        token: String,
        userId: String,
        name: String,
        email: String
    ) {
        dataStore.edit {
            it[TOKEN] = token
            it[USER_ID] = userId
            it[USER_NAME] = name
            it[USER_EMAIL] = email
        }
    }

    suspend fun clearSession() {
        dataStore.edit {
            it.remove(TOKEN)
            it.remove(USER_ID)
            it.remove(USER_NAME)
            it.remove(USER_EMAIL)
        }
    }
}
