package com.shopease.app.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Since we're using manual DI (AppContainer) instead of Hilt, ViewModels with
 * constructor dependencies need a factory to be created via viewModel { }.
 * This generic factory just wraps a lambda that builds the ViewModel, so each
 * screen only needs a one-liner instead of a bespoke Factory class.
 */
class ViewModelFactory<T : ViewModel>(
    private val create: () -> T
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM {
        return create() as VM
    }
}
