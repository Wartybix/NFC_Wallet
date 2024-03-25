package com.example.nfcwallet

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayOutputStream
import java.io.Serializable

class Tag(
    var name: String
) : Serializable {
    private var image: ByteArray? = null

    fun getImage() : ImageBitmap? {
        return if (image == null) {
            null
        } else {
            BitmapFactory.decodeByteArray(image, 0, image!!.size).asImageBitmap()
            //TODO do something about this non-null asserted call
        }
    }

    fun setImage(newImage: ImageBitmap?) {
        if (newImage == null) {
            image = null
            return
        }

        val byteStream = ByteArrayOutputStream()
        newImage.asAndroidBitmap().compress(Bitmap.CompressFormat.PNG, 0, byteStream)
        image = byteStream.toByteArray()
    }
}