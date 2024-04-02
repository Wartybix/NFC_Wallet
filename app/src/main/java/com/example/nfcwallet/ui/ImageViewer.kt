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
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable

@Composable
fun ImageViewer(
    image: ImageBitmap,
    modifier: Modifier = Modifier
) {
    val zoomState = rememberZoomState()

    Image(
        bitmap = image,
        contentDescription = null,
        modifier = modifier
            .fillMaxSize()
            .zoomable(zoomState)
    )
}

@Preview
@Composable
fun ImageViewerPreview() {
    NFCWalletTheme {
        ImageViewer(image = ImageBitmap.imageResource(R.drawable.pigeon))
    }
}