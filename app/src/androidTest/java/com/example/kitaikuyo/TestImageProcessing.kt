package com.example.kitaikuyo

import android.graphics.Bitmap
import androidx.test.platform.app.InstrumentationRegistry
import com.example.kitaikuyo.utils.ImageProcessing
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import kotlin.system.measureTimeMillis


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class TestImageProcessing {

    @Test
    fun `use_app_context`() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.kitaikuyo", appContext.packageName)
    }

    @Test
    fun `test_load_image_as_bitmap_found_image`() {
        val imageProcessing = ImageProcessing()
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        var resultBitmapFromAssetImage: Bitmap =
            imageProcessing.loadImageAsBitmap(appContext.assets, "original-image.jpg")
        println(resultBitmapFromAssetImage.toString())
    }

    @Test
    fun `test_load_image_as_bitmap_not_found_image`() {
        val imageProcessing = ImageProcessing()
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        assertThrows(IOException::class.java){
            imageProcessing.loadImageAsBitmap(appContext.assets, "unknown.jpg")
        }
    }

    @Test
    fun `test_convert_bitmap_to_byte_buffer`(){
        val imageProcessing = ImageProcessing()
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        var resultBitmapFromAssetImage: Bitmap =
            imageProcessing.loadImageAsBitmap(appContext.assets, "original-image.jpg")

        val resultByteBufferFromBitmap: ByteBuffer = imageProcessing.convertBitmapToByteBuffer(resultBitmapFromAssetImage)
        println(resultByteBufferFromBitmap.toString())
    }

    @Test
    fun `test_convert_bitmap_to_byte_buffer_time_milisecond`(){
        val imageProcessing = ImageProcessing()
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        var resultBitmapFromAssetImage: Bitmap =
            imageProcessing.loadImageAsBitmap(appContext.assets, "original-image.jpg")

        // result time
        val time = measureTimeMillis {
            val resultByteBufferFromBitmap: ByteBuffer = imageProcessing.convertBitmapToByteBuffer(resultBitmapFromAssetImage)
            println(resultByteBufferFromBitmap.toString())
        }
        println("result time for test_convert_bitmap_to_byte_buffer_time_milisecond: $time")
        // end result time
    }



    @Test
    //[0.0, 1.0]
    fun `test_convert_bitmap_to_byte_buffer_not_return_minus`(){
        val imageProcessing: ImageProcessing = ImageProcessing()
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        var resultBitmapFromAssetImage: Bitmap =
            imageProcessing.loadImageAsBitmap(appContext.assets, "original-image.jpg")

        val resultByteBufferFromBitmap: ByteBuffer = imageProcessing.convertBitmapToByteBuffer(resultBitmapFromAssetImage)


        resultByteBufferFromBitmap.rewind()
        var floatBuffer: FloatBuffer = resultByteBufferFromBitmap.asFloatBuffer()
        var floatArray = FloatArray(floatBuffer.remaining())
        floatBuffer.get(floatArray)


    }




}