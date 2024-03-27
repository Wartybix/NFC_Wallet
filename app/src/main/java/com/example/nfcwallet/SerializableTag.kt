package com.example.nfcwallet

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayOutputStream
import java.io.Serializable

class SerializableTag(val name: String, image: ImageBitmap?) : Serializable {
    private var serializableImage: ByteArray? = null

    init {
        serializableImage = if (image == null) {
            null
        } else {
            val byteStream = ByteArrayOutputStream()

            val compressFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                Bitmap.CompressFormat.WEBP_LOSSY
            else
                Bitmap.CompressFormat.JPEG

            image.asAndroidBitmap().compress(compressFormat, 0, byteStream)
            byteStream.toByteArray()
        }
    }

    fun getImage() : ImageBitmap? {
        return if (serializableImage == null) {
            null
        } else {
            BitmapFactory.decodeByteArray(
                serializableImage,
                0,
                serializableImage!!.size
            ).asImageBitmap()
        }
    }
}