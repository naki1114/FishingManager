package com.example.fishingmanager.function

import java.util.Random

class GetRandomResult {

    fun getRandomResult() : String {

        var random = Random()
        var result = ""
        var type = 0
        var i = 0
        var c : Char

        for (j in 0..5) {

            i = random.nextInt(9)
            result += i.toString()

        }

        return result

    } // getRandomResult()


}