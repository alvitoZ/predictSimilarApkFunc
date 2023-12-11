package com.example.kitaikuyo.utils

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.RectF
import com.example.kitaikuyo.InputModelSize
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.random.Random


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

    fun bytebufferToBitmap(output: ByteBuffer): Bitmap {
        var value = 30
        var value2 = 224
        output?.rewind() // Rewind the output buffer after running.

        val bitmap = Bitmap.createBitmap(value2, value2, Bitmap.Config.ARGB_8888)
        val pixels = IntArray(value2 * value2) // Set your expected output's height and width
        for (i in 0 until 224 * 224) {
//        for (i in 0 until value * value) {
            val a = 0xFF
            val r: Float = output?.float!! * 255.0f
            val g: Float = output?.float!! * 255.0f
            val b: Float = output?.float!! * 255.0f
            pixels[i] = a shl 24 or (r.toInt() shl 16) or (g.toInt() shl 8) or b.toInt()
        }
        bitmap.setPixels(pixels, 0, value2, 0, 0, value2, value2)

        return bitmap
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


    fun createEmptyBitmap(imageWidth: Int, imageHeight: Int, color: Int = 0): Bitmap {
        val ret = Bitmap.createBitmap(imageWidth, imageHeight, Bitmap.Config.RGB_565)
        if (color != 0) {
            ret.eraseColor(color)
        }
        return ret
    }



    companion object {
        private const val BATCH_SIZE = InputModelSize.BATCH_SIZE
        private const val INPUT_SIZE = InputModelSize.INPUT_SIZE
        private const val PIXEL_SIZE = InputModelSize.PIXEL_SIZE
        private const val FLOAT_TYPE_SIZE = InputModelSize.FLOAT_TYPE_SIZE
    }

}