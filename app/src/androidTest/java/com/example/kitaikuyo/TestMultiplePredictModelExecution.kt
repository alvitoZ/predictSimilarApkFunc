package com.example.kitaikuyo

import android.graphics.Bitmap
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import com.example.kitaikuyo.utils.ImageProcessing
import com.example.kitaikuyo.utils.InterpreterInitialized
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.tensorflow.lite.Interpreter
import kotlin.system.measureTimeMillis


class TestMultiplePredictModelExecution {

    lateinit var interpreterInitialized : InterpreterInitialized;

    @Before
    fun `initialize_interpreter`(){
        // use appContext
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        // end use appContext

        // run interpreter
        interpreterInitialized = InterpreterInitialized(appContext)

        // initialize interpreter
        interpreterInitialized.initialize()

        // end run interpreter
    }

    @After
    fun `close_interpreter`(){
        // close initialize interpreter
        interpreterInitialized.close()
        // end run interpreter
    }

    @Test
    fun `test_get_predict_image_success`(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        // get image list from assets
            val folder = appContext.assets
            val list = mutableListOf<String>()
            val int = folder.list(FOLDER_PATH)!!.size - 1

            for (i in 0..int){
                folder.list(FOLDER_PATH)?.get(i)?.let { list.add(it) }
            }
        // end get image list from assets

        assertEquals("size of images in FOLDER_PATH in assets", int, folder.list(FOLDER_PATH)!!.size - 1)
    }

    @Test
    fun test_get_predict_image_failed(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        // get image list from assets
        val folder = appContext.assets
        val list = mutableListOf<String>()
        val total = folder.list(WRONG_FOLDER_PATH)!!.size - 1

        for (i in 0..total){
            folder.list(WRONG_FOLDER_PATH)?.get(i)?.let { list.add(it) }
        }

        // end get image list from assets

        assertEquals("size of images in WRONG_FOLDER_PATH in assets", 0, folder.list(WRONG_FOLDER_PATH)!!.size )
    }

    @Test
    fun `test_execute_async_success`(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        // get image list from assets
        val folder = appContext.assets
        val list = mutableListOf<String>()
        val total = folder.list(FOLDER_PATH)!!.size - 1

        for (i in 0..total){
            folder.list(FOLDER_PATH)?.get(i)?.let { list.add(it) }
        }
        // end get image list from assets

        // use interpreter and execute model with input image and image folder
        val multiplePredictModelExecution = MultiplePredictModelExecution(interpreterInitialized.interpreter, interpreterInitialized.isInitialized)
        val resultFromMultiplePredictModelExecution = multiplePredictModelExecution.executeAsync(appContext, IMAGE_PATH, list, FOLDER_PATH)
        resultFromMultiplePredictModelExecution.addOnSuccessListener {
            assertTrue("result must be bitmap", it.bitmapResize is Bitmap)
            assertEquals("result of bitmap width must be 224", 224, it.bitmapResize.width)
            assertEquals("result of bitmap height must be 224", 224, it.bitmapResize.height)
            assertEquals("result of multiplePredictResult size must be $total", total, it.multiplePredictResult.size - 1)
        }

    }

    @Test
    fun `test_execute_async_failed`(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        // get image list from assets
        val folder = appContext.assets
        val list = mutableListOf<String>()
        val total = folder.list(WRONG_FOLDER_PATH)!!.size - 1

        for (i in 0..total){
            folder.list(WRONG_FOLDER_PATH)?.get(i)?.let { list.add(it) }
        }
        // end get image list from assets

        // use interpreter and execute model with input image and image folder
        val multiplePredictModelExecution = MultiplePredictModelExecution(interpreterInitialized.interpreter, interpreterInitialized.isInitialized)
        val resultFromMultiplePredictModelExecution = multiplePredictModelExecution.executeAsync(appContext, WRONG_IMAGE_PATH, list, WRONG_FOLDER_PATH)

//        assertThrows(IOException::class.java){
            resultFromMultiplePredictModelExecution.addOnFailureListener {
                println("$it miaw desuwa")
            }
//        }


    }

    @Test
    fun `test_execute_async_image_bitmap_same_as_bitmap_resize`(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        // get image list from assets
        val folder = appContext.assets
        val list = mutableListOf<String>()
        val total = folder.list(FOLDER_PATH)!!.size - 1

        for (i in 0..total){
            folder.list(FOLDER_PATH)?.get(i)?.let { list.add(it) }
        }
        // end get image list from assets

        // load image as Bitmap
        val imageProcessing = ImageProcessing()
        val bitmap: Bitmap = imageProcessing.loadImageAsBitmap(appContext.assets, IMAGE_PATH)
        val pixels = IntArray(INPUT_SIZE * INPUT_SIZE)
        // end load image as Bitmap

        // use interpreter and execute model with input image and image folder
        val multiplePredictModelExecution = MultiplePredictModelExecution(interpreterInitialized.interpreter, interpreterInitialized.isInitialized)
        val resultFromMultiplePredictModelExecution = multiplePredictModelExecution.executeAsync(appContext, IMAGE_PATH, list, FOLDER_PATH)
        resultFromMultiplePredictModelExecution.addOnSuccessListener {
            assertEquals("result of bitmap height must be same as bitmapResize", bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height), it.bitmapResize.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height))
        }
    }

    @Test
    fun `test_execute_async_time_milisecond`(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        // get image list from assets
        val folder = appContext.assets
        val list = mutableListOf<String>()
        val total = folder.list(FOLDER_PATH)!!.size - 1

        for (i in 0..total){
            folder.list(FOLDER_PATH)?.get(i)?.let { list.add(it) }
        }

        // end get image list from assets

        // use interpreter and execute model with input image and image folder
        // result time
        val time = measureTimeMillis {
            val multiplePredictModelExecution = MultiplePredictModelExecution(interpreterInitialized.interpreter, interpreterInitialized.isInitialized)
            val resultFromMultiplePredictModelExecution = multiplePredictModelExecution.executeAsync(appContext, IMAGE_PATH, list, FOLDER_PATH)
            resultFromMultiplePredictModelExecution.addOnSuccessListener {
                assertTrue("result must be bitmap", it.bitmapResize is Bitmap)
                assertEquals("result of bitmap width must be 224", 224, it.bitmapResize.width)
                assertEquals("result of bitmap height must be 224", 224, it.bitmapResize.height)
                assertTrue("result of multiplePredictResult[0].float must be float", it.multiplePredictResult[0].float is Float)
                assertTrue("result of multiplePredictResult[0].string must be string", it.multiplePredictResult[0].string is String)
                assertEquals("result of multiplePredictResult size must be $total", total, it.multiplePredictResult.size - 1)
            }
        }

        println("total time : $time")
        // end result time

    }



    companion object {
        private const val IMAGE_PATH = "original-image.jpg"
        private const val WRONG_IMAGE_PATH = "original-image.jpg"
        private const val FOLDER_PATH = "predict"
        private const val WRONG_FOLDER_PATH = "unknown"
        private const val INPUT_SIZE = 224
    }


}