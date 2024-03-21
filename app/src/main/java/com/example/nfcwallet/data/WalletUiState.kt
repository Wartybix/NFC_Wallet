package com.example.nfcwallet.data

import android.graphics.Bitmap
import android.nfc.Tag
import androidx.compose.runtime.snapshots.SnapshotStateList

data class WalletUiState(
    val projectionMode: Boolean = false,
    val tagName: String = "",
    val tagImage: Bitmap? = null,
    //val tags: SnapshotStateList<Tag>
)