package com.example.fishingmanager.tensorflowModel

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.label.TensorLabel
import org.tensorflow.lite.support.model.Model
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class TensorflowModel(val context: Context) {

    val TAG = "TensorflowModel"
    val MODEL_NAME = "fishmodel.tflite"
    val LABEL_FILE = "labels.txt"

    lateinit var inputImage: TensorImage
    lateinit var outputBuffer: TensorBuffer
    lateinit var model: Model
    lateinit var labels: List<String>

    var inputWidth = 0
    var inputHeight = 0
    var inputChannel = 0


    // 모델 초기화 작업 (*이미지 분류 학습된 딥러닝 로봇을 모델이라 통칭)
    fun init() {

        model = Model.createModel(context, MODEL_NAME)
        labels = FileUtil.loadLabels(context, LABEL_FILE)
        initModelShape()

    } // init()


    // 모델 형태 초기화
    fun initModelShape() {

        val inputTensor = model.getInputTensor(0)
        val shape = inputTensor.shape()
        inputChannel = shape[0]
        inputWidth = shape[1]
        inputHeight = shape[2]
        inputImage = TensorImage(inputTensor.dataType())

        val outputTensor = model.getOutputTensor(0)
        outputBuffer = TensorBuffer.createFixedSize(outputTensor.shape(), outputTensor.dataType())

    } // initModelShape()


    // ARGB_8888 비트맵 변환
    fun convertBitmap(bitmap: Bitmap): Bitmap {

        return bitmap.copy(Bitmap.Config.ARGB_8888, true)

    } // convertBitmap()


    // 이미지 불러오기
    fun loadImage(bitmap: Bitmap): TensorImage {

        if (bitmap.config != Bitmap.Config.ARGB_8888) {
            inputImage.load(convertBitmap(bitmap))
        } else {
            inputImage.load(bitmap)
        }

        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(inputWidth, inputHeight, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
            .add(NormalizeOp(0.0f, 255.0f))
            .build()

        return imageProcessor.process(inputImage)

    } // loadImage()


    // 이미지 분류 작업
    fun classify(bitmap : Bitmap) : Pair<String, Float> {

        inputImage = loadImage(bitmap)
        val inputs = arrayOf(inputImage.buffer)
        val outputs = HashMap<Int, Any>()

        outputs[0] = outputBuffer.buffer.rewind()
        model.run(inputs, outputs)

        val output = TensorLabel(labels, outputBuffer).mapWithFloatValue

        return argmax(output)

    } // classify()


    // 분류한 이미지로 결과 도출
    fun argmax(map : Map<String, Float>) : Pair<String, Float> {

        var maxKey = ""
        var maxVal = -1f

        for ((key, value) in map) {

            var f = value

            if (f > maxVal) {

                maxKey = key
                maxVal = f

            }

        }

        return Pair(maxKey, maxVal)

    } // argmax()


    // 모델 종료
    fun closeModel() {

        model.close()

    } // closeModel()


}