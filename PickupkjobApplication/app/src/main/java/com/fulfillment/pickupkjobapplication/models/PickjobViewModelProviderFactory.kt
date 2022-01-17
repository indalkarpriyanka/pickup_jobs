package com.fulfillment.pickupkjobapplication.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PickjobViewModelProviderFactory(val pickjobRepository: PickjobRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PickjobViewModel(pickjobRepository) as T
    }
}