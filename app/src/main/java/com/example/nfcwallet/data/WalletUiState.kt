package com.example.nfcwallet.data

import android.graphics.Bitmap

data class WalletUiState(
    val projectionMode: Boolean = false,
    val tagName: String = "",
    val tagImage: Bitmap? = null
)