package com.shopease.app.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val isLoggedIn: Flow<Boolean>
    val userId: Flow<String?>
    val userName: Flow<String?>
    val userEmail: Flow<String?>
    suspend fun login(userId: String, name: String, email: String)
    suspend fun logout()
}
