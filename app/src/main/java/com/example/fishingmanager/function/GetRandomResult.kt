package com.example.fishingmanager.function

import java.util.Random

class GetRandomResult {


    // 랜덤 값 추출
    fun getRandomResult(): String {

        var random = Random()
        var result = ""
        var i = 0

        for (j in 0..5) {

            i = random.nextInt(9)
            result += i.toString()

        }

        return result

    } // getRandomResult()


}