package com.example.kitaikuyo

import android.graphics.Bitmap
import java.nio.ByteBuffer

//data class ExecutionResult (
//    val bitmapResize:Bitmap,
////    val outputBufferString:String,
//    val outputBuffer:ByteBuffer,
////    val maskOnlyBitmap:Bitmap,
////    val inputBufferString:String
//)

 data class Result(
    val float: Float,
    val string: String,
)

data class ExecutionResult (
    val multiplePredictResult:MutableList<Result>,
    val bitmapResize: Bitmap,
//    val byteBufferToBitmap: Bitmap
)

data class ExecutionResult2 (
    val singlePredictResult:Result,
    val bitmapResize: Bitmap,
//    val byteBufferToBitmap: Bitmap
)
