package com.example.gallerychicago.Data

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ArtworkViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArtworkViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ArtworkViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
