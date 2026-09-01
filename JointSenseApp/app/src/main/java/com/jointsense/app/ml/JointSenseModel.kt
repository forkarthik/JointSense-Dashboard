package com.jointsense.app.ml

import android.content.Context
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class JointSenseModel(context: Context) {

    private var interpreter: Interpreter? = null

    init {
        try {
            val modelBuffer = loadModelFile(context, "jointsense_model.tflite")
            val options = Interpreter.Options()
            options.setNumThreads(4)
            interpreter = Interpreter(modelBuffer, options)
            Log.d("JointSenseModel", "TFLite Model Loaded Successfully")
        } catch (e: Exception) {
            Log.e("JointSenseModel", "Error loading TFLite Model", e)
        }
    }

    private fun loadModelFile(context: Context, modelName: String): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(modelName)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        return fileChannel.map(
            FileChannel.MapMode.READ_ONLY,
            fileDescriptor.startOffset,
            fileDescriptor.declaredLength
        )
    }

    /**
     * Runs inference on the provided sensor data.
     * @param sensorData A float array of shape [10, 3] representing 10 time steps of 3 features (accel, piezo, flexion)
     * @return The predicted OA risk score (0.0 to 1.0)
     */
    fun analyzeRisk(sensorData: Array<FloatArray>): Float {
        if (interpreter == null) {
            Log.e("JointSenseModel", "Interpreter not initialized")
            return 0f
        }

        // Input shape for the model is [1, 10, 3]
        val input = Array(1) { sensorData }

        // Output shape is [1, 1]
        val output = Array(1) { FloatArray(1) }

        return try {
            interpreter?.run(input, output)
            output[0][0] * 100f // Return as percentage 0-100
        } catch (e: Exception) {
            Log.e("JointSenseModel", "Error running inference", e)
            0f
        }
    }

    fun close() {
        interpreter?.close()
        interpreter = null
    }
}
