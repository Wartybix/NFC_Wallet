package com.example.nfcwallet

import androidx.compose.ui.graphics.ImageBitmap
import java.io.Serializable

class Tag(
    var name: String,
    var image: ImageBitmap?,
) : Serializable