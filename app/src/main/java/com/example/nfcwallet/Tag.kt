package com.example.nfcwallet

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import java.io.Serializable

class Tag(
    var name: String
) : Serializable {
    private var image: ByteArray? = null

    fun getImage() : Bitmap? {
        return if (image == null) {
            null
        } else {
            BitmapFactory.decodeByteArray(image, 0, image!!.size)
            //TODO do something about this non-null asserted call
        }
    }

    fun setImage(newImage: Bitmap?) {
        if (newImage == null) {
            image = null
            return
        }

        val byteStream = ByteArrayOutputStream()
        newImage.compress(Bitmap.CompressFormat.PNG, 0, byteStream)
        image = byteStream.toByteArray()
    }
}