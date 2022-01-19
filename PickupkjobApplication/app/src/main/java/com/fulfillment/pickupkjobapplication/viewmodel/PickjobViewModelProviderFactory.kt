package com.fulfillment.pickupkjobapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fulfillment.pickupkjobapplication.repository.PickjobRepository

class PickjobViewModelProviderFactory(private val pickjobRepository: PickjobRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PickjobViewModel(pickjobRepository) as T
    }
}