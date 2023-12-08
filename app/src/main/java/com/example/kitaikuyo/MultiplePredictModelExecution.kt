package com.example.kitaikuyo

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import org.tensorflow.lite.Interpreter
import com.example.kitaikuyo.utils.ImageProcessing
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.sqrt
import kotlin.math.pow

class MultiplePredictModelExecution(val interpreter: Interpreter?, var interpreterIsInitialized:Boolean) {

    private var imageProcessing: ImageProcessing = ImageProcessing()

    private var bitmapResize: Bitmap = imageProcessing.createEmptyBitmap(INPUT_SIZE, INPUT_SIZE)


    /** Executor to run inference task in the background */
    private val executorService: ExecutorService = Executors.newFixedThreadPool(20)


    private fun cosineSim(vector1:FloatArray, vector2:FloatArray):Float{
        var sum = 0.0f
        var suma1 = 0.0f
        var suma2 = 0.0f

//        var zipped: List<Pair<Float, Float>> = vector1 zip vector2
        var zipped: List<Pair<Float, Float>> = vector1.zip(vector2)

        for ((i,j) in zipped){
            suma1 += i * j
            suma2 += j * j
            sum += i * j
        }
        var cosine_sim = sum / (sqrt(suma1) * sqrt(suma2))

        return cosine_sim

    }

    private fun cosineSim2(vector1:FloatArray, vector2:FloatArray):Float{
        var dotProduct = 0.0
        var normVector1 = 0.0
        var normVector2 = 0.0

        for (i in vector1.indices){
            dotProduct += vector1[i] * vector2[i]
            normVector1 += vector1[i] * vector1[i]
            normVector2 += vector2[i] * vector2[i]
        }

        var similarity = dotProduct / (sqrt(normVector1) * sqrt(normVector2))

        return similarity.toFloat()
    }

    private fun cosineSim3(vector1:FloatArray, vector2:FloatArray):Float{
        var dotProduct = 0.0
        var normVector1 = 0.0
        var normVector2 = 0.0
        var int = vector1.size - 1

        for (i in 0..int){
            dotProduct += vector1[i] * vector2[i]
            normVector1 += vector1[i].pow(2)
            normVector2 += vector2[i].pow(2)
        }

        var cosinesim = dotProduct / (sqrt(normVector1) * sqrt(normVector2))
        return cosinesim.toFloat()
    }


    fun cosineSim4(vector1:FloatArray, vector2:FloatArray):Float{
        var dotProduct = 0.0;
        var magnitude1 = 0.0;
        var magnitude2 = 0.0;
        var cosineSimilarity = 0.0;

        var int = vector1.size -1

        for ( i in 0..int)
        {
            dotProduct += vector1[i] * vector2[i];  //a.b
            magnitude1 += vector1[i].pow(2);  //(a^2)
            magnitude2 += vector2[i].pow(2); //(b^2)
        }

        magnitude1 = sqrt(magnitude1);//sqrt(a^2)
        magnitude2 = sqrt(magnitude2);//sqrt(b^2)

        if (magnitude1 != 0.0 || magnitude2 != 0.0) {
            cosineSimilarity = dotProduct / (magnitude1 * magnitude2)
        }
        else {
            return 0.0f
        }
        return cosineSimilarity.toFloat();
    }

    private fun byteBufferToFloatArray(byteBuffer: ByteBuffer):FloatArray{
        byteBuffer.rewind()
        var floatBuffer: FloatBuffer = byteBuffer.asFloatBuffer()
        var floatArray = FloatArray(floatBuffer.remaining())
        floatBuffer.get(floatArray)

        return floatArray
    }

    private fun execute(bitmap: Bitmap): ByteBuffer {
        if (!interpreterIsInitialized) {
            throw IllegalStateException("TF Lite Interpreter is not initialized yet.")
        }

        var startTime: Long
        var elapsedTime: Long

        // Preprocessing: resize the input
        startTime = System.nanoTime()


//        val resizedImage = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, true)

        val inputBuffer = imageProcessing.convertBitmapToByteBuffer(bitmap)

        val outputBuffer = ByteBuffer.allocateDirect(OUTPUT_SIZE * FLOAT_TYPE_SIZE)
        outputBuffer.order(ByteOrder.nativeOrder())
        outputBuffer.rewind()

        elapsedTime = (System.nanoTime() - startTime) / 1000000
        Log.d(TAG, "Preprocessing time = " + elapsedTime + "ms")

        startTime = System.nanoTime()

        //will adding try catch block
        try {
            interpreter?.run(inputBuffer, outputBuffer)
        }
        catch (e:IllegalThreadStateException){
            throw IllegalThreadStateException("failed to run interpreter $e")
        }
        //in here later


        elapsedTime = (System.nanoTime() - startTime) / 1000000
        Log.d(TAG, "Inference time = " + elapsedTime + "ms")


        return outputBuffer
    }

    private fun predictMultiple(assetManager: AssetManager, filePath: String, folderDir: MutableList<String>, folderPath:String):MutableList<Result>{
        var list = mutableListOf<Result>()

        for (image in folderDir){
            list.add(
                Result(
                    cosineSim4(
                        byteBufferToFloatArray(
                            execute(imageProcessing.loadImageAsBitmap(assetManager, filePath))
                        ),
                        byteBufferToFloatArray(
                            execute(imageProcessing.loadImageAsBitmap(assetManager,"$folderPath/$image"))
                        ),
                    ),
                    image
                )
            )
        }

        return list
    }

    fun executeAsync(context: Context ,filePath: String, folderDir: MutableList<String>, folderPath:String): Task<ExecutionResult2> {
        val task = TaskCompletionSource<ExecutionResult2>()
        // i will delete
        bitmapResize = Bitmap.createScaledBitmap(imageProcessing.loadImageAsBitmap(context.assets, filePath), INPUT_SIZE, INPUT_SIZE, true)
        // this later
        executorService.execute {
            val result = predictMultiple(context.assets ,filePath, folderDir, folderPath)
            task.setResult(
                ExecutionResult2(
                    result,
                    bitmapResize
                )
            )
        }
//        executorService.shutdown()
        return task.task
    }

    companion object {
        private const val FAILED_TAG = "FailedModelExecution"
        private const val SUCCESS_TAG = "SuccessModelExecution"
        private const val TAG = "ModelExecution"
        private const val INPUT_SIZE = 224
        private const val OUTPUT_SIZE = 1280
        private const val FLOAT_TYPE_SIZE = 4
    }

}