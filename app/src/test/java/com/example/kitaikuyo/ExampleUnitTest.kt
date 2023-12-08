package com.example.kitaikuyo

import com.example.kitaikuyo.utils.ImageProcessing
import org.junit.Assert.assertEquals
import org.junit.Test
/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
//        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals(4, 2 + 2)
    }

    @Test
    fun coba(){

        println(1)

//        assertEquals(4,  ImageProcessing().coba(2))
    }
}