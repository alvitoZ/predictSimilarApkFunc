package com.example.kitaikuyo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.kitaikuyo.utils.ImageProcessing
import com.example.kitaikuyo.utils.InterpreterInitialized

//class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//    }
//}

class MainActivity : AppCompatActivity() {
    private var imageView: ImageView? = null
    private var actionButton: Button? = null
    private var clearButton: Button? = null
    private var predictedTextView: TextView? = null

    private var imageProcessing: ImageProcessing = ImageProcessing()
    private var interpreterInitialized: InterpreterInitialized = InterpreterInitialized(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        clearButton = findViewById(R.id.clear_button)
        actionButton = findViewById(R.id.action_button)
        imageView = findViewById(R.id.result_imageview)
        predictedTextView = findViewById(R.id.predicted_text)

        // Setup clear drawing button
        clearButton?.setOnClickListener {
            imageView?.setImageBitmap(imageProcessing.createEmptyBitmap(224,224))
            predictedTextView?.text = "clear success"
        }

        actionButton?.setOnClickListener {
            triggerAction()
        }

        interpreterInitialized
            .initialize()
//      .addOnSuccessListener {e  -> Log.e(FAILED_TAG, "Error to setting up digit classifier.", e) }
            .addOnFailureListener { e -> Log.e(FAILED_TAG, "Error to setting up digit classifier.", e) }
    }

    override fun onDestroy() {
        interpreterInitialized.close()
        super.onDestroy()
    }

    private fun triggerAction() {
        MultiplePredictModelExecution(interpreterInitialized.interpreter, interpreterInitialized.isInitialized).
        executeAsync(this,"original-image.jpg", getPredictImage(), FOLDER_PATH).
        addOnSuccessListener {
            Log.i(SUCCESS_TAG, "${it.multiplePredictResult.toList()}")
            imageView?.setImageBitmap(it.bitmapResize)
            predictedTextView?.text = "success"
        }.
        addOnFailureListener {
            Log.i(FAILED_TAG, "$it")
            predictedTextView?.text = "failed to MultiplePredictModelExecution: $it"
        }
    }

    fun Consoled(tag:String ,str: String){
        Log.i(tag, "${str}")
    }

//  fun coba():Unit{
//
//    var list = mutableListOf<Int>(1,2,3,4,5,6)
//    var list2 = mutableListOf<Int>(1,2,3,4,5,6,324,424,3)
//    for ((i,j) in list zip list2){
//      println("i :$i j: $j")
//    }
//  }

    fun getPredictImage():MutableList<String>{
        val coba = this.assets
        val list = mutableListOf<String>()
        val int = coba.list("$FOLDER_PATH")!!.size - 1

        for (i in 0..int){
            coba.list("$FOLDER_PATH")?.get(i)?.let { list.add(it) }
        }
        return list
    }


    companion object {
        private const val FAILED_TAG = "FailedMainActivity"
        private const val SUCCESS_TAG = "SuccessMainActivity"
        private const val TAGINPUT = "KITAN"
        private const val TAGOUTPUT = "IKUYO"
        private const val FOLDER_PATH = "predict2"
    }
}
