package com.example.nfcwallet.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.nfcwallet.R
import com.example.nfcwallet.ui.theme.NFCWalletTheme

@Composable
fun ImageViewer(
    image: ImageBitmap,
    modifier: Modifier = Modifier
) {
    Image(
        bitmap = image,
        contentDescription = null,
        modifier = Modifier.fillMaxSize()
    )
}

@Preview
@Composable
fun ImageViewerPreview() {
    NFCWalletTheme {
        ImageViewer(image = ImageBitmap.imageResource(R.drawable.pigeon))
    }
}