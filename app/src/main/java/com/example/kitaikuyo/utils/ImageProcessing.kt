package com.example.kitaikuyo.utils

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder


class ImageProcessing {


    fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val resizedImage = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, true)
        val inputBuffer = ByteBuffer.allocateDirect( BATCH_SIZE * INPUT_SIZE * INPUT_SIZE * PIXEL_SIZE * FLOAT_TYPE_SIZE)
        inputBuffer.order(ByteOrder.nativeOrder())
        inputBuffer.rewind()

        val pixels = IntArray(INPUT_SIZE * INPUT_SIZE)
        resizedImage.getPixels(pixels, 0, resizedImage.width, 0, 0, resizedImage.width, resizedImage.height)
            for (pixelValue in pixels) {
                val r = (pixelValue shr 16 and 0xFF)
                val g = (pixelValue shr 8 and 0xFF)
                val b = (pixelValue and 0xFF)

                // Convert RGB to grayscale and normalize pixel value to [0..1]
                val normalizedPixelValue = (r + g + b) / 3.0f / 255.0f
                inputBuffer.putFloat(normalizedPixelValue)
            }

        inputBuffer.rewind()
        return inputBuffer
    }

    fun loadImageAsBitmap(assetmanager: AssetManager, image: String): Bitmap {
        val inputstream: InputStream
        try {
            inputstream = assetmanager.open(image)
        }catch (e: IOException) {
            throw IOException("gagal buka asset $image $e")
        }

        return BitmapFactory.decodeStream(inputstream)
    }

    fun coba(value:Int):Int{
        return value+ value

    }

    fun createEmptyBitmap(imageWidth: Int, imageHeight: Int, color: Int = 0): Bitmap {
        val ret = Bitmap.createBitmap(imageWidth, imageHeight, Bitmap.Config.RGB_565)
        if (color != 0) {
            ret.eraseColor(color)
        }
        return ret
    }

    fun returnBitmap(bitmap: Bitmap): Bitmap {
        return bitmap
    }

    companion object {
        private const val BATCH_SIZE = 1
        private const val INPUT_SIZE = 224
        private const val PIXEL_SIZE = 3
        private const val FLOAT_TYPE_SIZE = 4
    }
}