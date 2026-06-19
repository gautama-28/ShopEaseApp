package com.shopease.app.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: ProfileViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is ProfileEffect.ShowInvalidFormMessage ->
                    snackbarHostState.showSnackbar("Please enter a valid name and email")
            }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Profile") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (state.isLoggedIn) {
                SignedInContent(state, viewModel)
            } else {
                SignedOutContent(state, viewModel)
            }
        }
    }
}

@Composable
private fun SignedInContent(state: ProfileState, viewModel: ProfileViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(80.dp)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Text(
            text = state.name,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(text = state.email, style = MaterialTheme.typography.bodyMedium)

        OutlinedButton(
            onClick = { viewModel.setEvent(ProfileEvent.LogoutClicked) },
            modifier = Modifier.padding(top = 32.dp)
        ) {
            Text("Log Out")
        }
    }
}

@Composable
private fun SignedOutContent(state: ProfileState, viewModel: ProfileViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Sign in to ShopEase",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Track orders, save your wishlist, and check out faster.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )

        OutlinedTextField(
            value = state.nameInput,
            onValueChange = { viewModel.setEvent(ProfileEvent.NameInputChanged(it)) },
            label = { Text("Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.emailInput,
            onValueChange = { viewModel.setEvent(ProfileEvent.EmailInputChanged(it)) },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )

        Button(
            onClick = { viewModel.setEvent(ProfileEvent.SignInClicked) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            Text("Sign In")
        }
    }
}
