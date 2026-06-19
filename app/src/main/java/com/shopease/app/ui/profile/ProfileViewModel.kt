package com.shopease.app.ui.profile

import androidx.lifecycle.viewModelScope
import com.shopease.app.domain.repository.UserPreferencesRepository
import com.shopease.app.ui.base.BaseViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.UUID

class ProfileViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : BaseViewModel<ProfileState, ProfileEvent, ProfileEffect>(ProfileState()) {

    init {
        combine(
            userPreferencesRepository.isLoggedIn,
            userPreferencesRepository.userName,
            userPreferencesRepository.userEmail
        ) { isLoggedIn, name, email ->
            Triple(isLoggedIn, name.orEmpty(), email.orEmpty())
        }.onEach { (isLoggedIn, name, email) ->
            setState { copy(isLoggedIn = isLoggedIn, name = name, email = email) }
        }.launchIn(viewModelScope)
    }

    override fun handleEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.NameInputChanged -> setState { copy(nameInput = event.value) }
            is ProfileEvent.EmailInputChanged -> setState { copy(emailInput = event.value) }
            is ProfileEvent.SignInClicked -> signIn()
            is ProfileEvent.LogoutClicked -> viewModelScope.launch {
                userPreferencesRepository.logout()
            }
        }
    }

    private fun signIn() {
        if (!currentState.isSignInFormValid) {
            setEffect { ProfileEffect.ShowInvalidFormMessage }
            return
        }
        viewModelScope.launch {
            // No real backend/auth here — a locally generated id plays the role
            // a server-issued user id would in a real sign-in flow.
            userPreferencesRepository.login(
                userId = UUID.randomUUID().toString(),
                name = currentState.nameInput.trim(),
                email = currentState.emailInput.trim()
            )
        }
    }
}
