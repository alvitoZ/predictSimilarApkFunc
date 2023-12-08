package com.example.kitaikuyo

import android.graphics.Bitmap
import androidx.test.platform.app.InstrumentationRegistry
import com.example.kitaikuyo.utils.ImageProcessing
import org.junit.After
import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.IOException


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleInstrumentedTest {

    var c:Int = 0

    @Before
    fun coba1(){
        c = c+1
        println("miaw1 $c" )
    }

    @After
    fun coba2(){
        c = c+1
        println("miaw1 $c")
    }

    @Test
    fun Coba3(){
        c = c+1
        println("miaw3 $c")
    }



    @Test
    fun Coba4(){
        c = c+1
        println("miaw4 $c")
    }


//    fun useAppContext() {
//        // Context of the app under test.
//        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
//        assertEquals("com.example.kitaikuyo", appContext.packageName)
//    }

//    @Test
//    fun testImageProcessing() {
//        val imageProcessing: ImageProcessing = ImageProcessing()
//        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
//        var resultBitmapFromAssetImage: Bitmap = imageProcessing.loadImageAsBitmap(appContext.assets, "original-image.jpg")
//        assertNotNull(resultBitmapFromAssetImage)
//
//        assertThrows(IOException::class.java){
//            imageProcessing.loadImageAsBitmap(appContext.assets, "unknown.jpg")
//        }


    }
