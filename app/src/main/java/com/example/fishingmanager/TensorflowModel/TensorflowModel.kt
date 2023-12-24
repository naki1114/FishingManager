package com.example.fishingmanagerclone.TensorflowModel

import android.content.Context
import android.graphics.Bitmap
import okhttp3.internal.lockAndWaitNanos
import org.tensorflow.lite.Tensor
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.label.TensorLabel
import org.tensorflow.lite.support.model.Model
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer


class TensorflowModel(context: Context) {

    var context : Context
    val MODEL_NAME : String = "model.tflite"
    val LABEL_FILE : String = "label.txt"
    lateinit var inputImage : TensorImage
    lateinit var outputBuffer : TensorBuffer
    lateinit var model : Model
    var modelInputWidth : Int = 0
    var modelInputHeight : Int = 0
    var modelInputChannel : Int = 0
    lateinit var labels : List<String>

    init {
        this.context = context
    }

    fun init() {

        model = Model.createModel(context, MODEL_NAME)
        labels = FileUtil.loadLabels(context, LABEL_FILE)

        initModelShape()

    } // init()


    fun initModelShape() {

        var inputTensor : Tensor = model.getInputTensor(0)
        var shape : IntArray? = inputTensor.shape()
        modelInputChannel = shape!![0]
        modelInputWidth = shape[1]
        modelInputHeight = shape[2]

        inputImage = TensorImage(inputTensor.dataType())

        var outputTensor : Tensor = model.getOutputTensor(0)
        outputBuffer = TensorBuffer.createFixedSize(outputTensor.shape(), outputTensor.dataType())

    } // initModelShape()


    fun convertBitmapToARGB8888(bitmap : Bitmap) : Bitmap {

        return bitmap.copy(Bitmap.Config.ARGB_8888, true)

    } // convertBitmapToARGB8888()


    fun loadImage(bitmap : Bitmap) : TensorImage {

        if (bitmap.config != Bitmap.Config.ARGB_8888) {

            inputImage.load(convertBitmapToARGB8888(bitmap))

        } else {

            inputImage.load(bitmap)

        }

        var imageProcessor : ImageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(modelInputWidth, modelInputHeight, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
            .add(NormalizeOp(0.0f, 255.0f))
            .build()

        return imageProcessor.process(inputImage)

    } // loadImage()


//    fun classify(bitmap : Bitmap) : Pair<String, Float> {
//
//        inputImage = loadImage(bitmap)
//        var inputs : Any[] = Any[]{inputImage.buffer}
//        var outputs : Map<Int, Any> = HashMap()
//
//        model.run(inputs, outputs)
//
//        var output : Map<String, Float> = TensorLabel(labels, outputBuffer).mapWithFloatValue
//        return argmax(output)
//
//    } // classify()
//
//
//    fun argmax(map : Map<String, Float>) : Pair<String, Float> {
//
//        var maxKey : String = ""
//        var maxVal : Float = (-1).toFloat()
//
//        for (entry : Map.Entry<String, Float> : map.entries) {
//
//            var f : Float = entry.getValue()
//
//            if (f > maxVal) {
//
//                maxKey = entry.getKey()
//                maxVal = f
//
//            }
//
//        }
//
//        return Pair(maxKey, maxVal)
//
//    } // argmax()


    fun finish() {

        if (model != null) {

            model.close()

        }

    } // finish()

}