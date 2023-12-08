package com.example.kitaikuyo.utils

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.io.IOException
import java.nio.channels.FileChannel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.nio.MappedByteBuffer

class InterpreterInitialized(private val context: Context) {

    var interpreter: Interpreter? = null

    var isInitialized = false
        private set

    /** Executor to run inference task in the background */
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    fun initialize(): Task<Void> {
        val task = TaskCompletionSource<Void>()
        executorService.execute {
            try {
                initializeInterpreter()
                task.setResult(null)
                Log.d(SUCCESS_TAG, "success to initialize interpreter")
            } catch (e: IOException) {
                task.setException(e)
                Log.d(FAILED_TAG, "failed to initialize interpreter $e")
            }
        }
        return task.task
    }

    @Throws(IOException::class)
    private fun initializeInterpreter() {
        // Load the TF Lite model
        val assetManager = context.assets
        val model = loadModelFile(assetManager)

        // Initialize TF Lite Interpreter with NNAPI enabled
        val options = Interpreter.Options()
        options.setUseNNAPI(true)
//    options.setNumThreads(4)
        val interpreter = Interpreter(model, options)

        // Finish interpreter initialization
        this.interpreter = interpreter
        isInitialized = true
        Log.d(TAG, "Initialized TFLite interpreter.")
    }


//    @Throws(IOException::class)
//    private fun getInterpreter(
//        context: Context,
//        modelName: String,
//        useGpu: Boolean = false
//    ): Interpreter {
//        val tfliteOptions = Interpreter.Options()
//        tfliteOptions.setNumThreads(numberThreads)
//
////        gpuDelegate = null
////        if (useGpu) {
////            gpuDelegate = GpuDelegate()
////            tfliteOptions.addDelegate(gpuDelegate)
////        }
//
//        return Interpreter(loadModelFile(context, modelName), tfliteOptions)
//    }

    @Throws(IOException::class)
    private fun loadModelFile(assetManager: AssetManager): MappedByteBuffer {
        val fileDescriptor = assetManager.openFd(MODEL_FILE)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        val retFile = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
        fileDescriptor.close()
        return retFile
    }


    fun close() {
        interpreter?.close()
//        if (gpuDelegate != null) {
//            gpuDelegate!!.close()
//        }
    }

    companion object {
        private const val FAILED_TAG = "FailedInterpreterInitialized"
        private const val SUCCESS_TAG = "SuccessInterpreterInitialized"
        private const val TAG = "InterpreterInitialized"
        private const val MODEL_FILE = "model4.tflite"
        private const val numberThreads = 4
    }

}

