package com.example.kitaikuyo

import androidx.test.platform.app.InstrumentationRegistry
import com.example.kitaikuyo.utils.InterpreterInitialized
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.system.measureTimeMillis


class InterpreterInitializedTest {
    @Test
    fun `use_app_context`() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.kitaikuyo", appContext.packageName)
    }

    @Test
    fun `test_interpreter_initialized_success`(){
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        // initialize interpreter
        val interpreterInitialized = InterpreterInitialized(appContext)
        interpreterInitialized.initialize()
        // end initialize interpreter

        interpreterInitialized.close()
    }

    @Test
    fun `test_interpreter_initialized_time_milisecond`(){
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        // initialize interpreter
        // result time
        val interpreterInitialized = InterpreterInitialized(appContext)

        val time = measureTimeMillis {
            interpreterInitialized.initialize()
        }
        println("result time initialized interpreter : $time")
        // end result time
        // end initialize interpreter

        // result time
        val timeClose = measureTimeMillis {
            interpreterInitialized.close()
        }
        println("result time close interpreter : $timeClose")
        // end result time

    }

//    @Test
    fun `test_interpreter_initialized_failed`(){
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        // initialize interpreter
        val interpreterInitialized = InterpreterInitialized(appContext)
        interpreterInitialized.initialize()
        // end initialize interpreter
    }
}