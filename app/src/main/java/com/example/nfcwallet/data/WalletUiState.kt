package com.example.nfcwallet.data

data class WalletUiState(
    val projectionMode: Boolean = false,
    val selectedTag: com.example.nfcwallet.Tag = com.example.nfcwallet.Tag("", null)
)