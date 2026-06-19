package com.shopease.app.ui.profile

import com.shopease.app.ui.base.UiEffect
import com.shopease.app.ui.base.UiEvent
import com.shopease.app.ui.base.UiState

data class ProfileState(
    val isLoggedIn: Boolean = false,
    val name: String = "",
    val email: String = "",
    // Sign-in form fields (only relevant while logged out)
    val nameInput: String = "",
    val emailInput: String = ""
) : UiState {
    val isSignInFormValid: Boolean
        get() = nameInput.isNotBlank() && emailInput.contains("@") && emailInput.contains(".")
}

sealed interface ProfileEvent : UiEvent {
    data class NameInputChanged(val value: String) : ProfileEvent
    data class EmailInputChanged(val value: String) : ProfileEvent
    data object SignInClicked : ProfileEvent
    data object LogoutClicked : ProfileEvent
}

sealed interface ProfileEffect : UiEffect {
    data object ShowInvalidFormMessage : ProfileEffect
}
