package com.shopease.app.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "shopease_prefs")

/**
 * Persists login/session state across app restarts. There's no real backend here,
 * so "login" just means: did the user go through the profile screen's sign-in flow,
 * and which local user id are they signed in as.
 */
class UserPreferencesDataStore(private val context: Context) {

    private object Keys {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val USER_ID = stringPreferencesKey("user_id")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.IS_LOGGED_IN] ?: false
    }

    val userId: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[Keys.USER_ID]
    }

    val userName: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[Keys.USER_NAME]
    }

    val userEmail: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[Keys.USER_EMAIL]
    }

    suspend fun setLoggedIn(userId: String, name: String, email: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.IS_LOGGED_IN] = true
            prefs[Keys.USER_ID] = userId
            prefs[Keys.USER_NAME] = name
            prefs[Keys.USER_EMAIL] = email
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { prefs ->
            prefs[Keys.IS_LOGGED_IN] = false
            prefs.remove(Keys.USER_ID)
            prefs.remove(Keys.USER_NAME)
            prefs.remove(Keys.USER_EMAIL)
        }
    }
}
