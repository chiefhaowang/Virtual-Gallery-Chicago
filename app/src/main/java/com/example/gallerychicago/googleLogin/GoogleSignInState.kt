package com.example.gallerychicago.googleLogin

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Stable
class GoogleSignInState(open: Boolean = false) {
    var opened by mutableStateOf(open)
        private set

    fun open() {
        opened = true
    }

    internal fun close() {
        opened = false
    }
}