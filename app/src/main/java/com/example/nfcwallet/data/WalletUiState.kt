package com.example.nfcwallet.data

import androidx.compose.ui.graphics.ImageBitmap

data class WalletUiState(
    val tagName: String = "",
    val tagImage: ImageBitmap? = null
)