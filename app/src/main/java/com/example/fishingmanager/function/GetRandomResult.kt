package com.example.fishingmanager.function

import java.util.Random

class GetRandomResult {

    fun getRandomResult() : String {

        var random = Random()
        var result = ""
        var type = 0
        var i = 0
        var c : Char

        for (j in 0..6) {

            type = random.nextInt(2)

            if (type == 0) {

                while (i < 65) {

                    i = random.nextInt(91)

                }

                c = i.toChar();
                result += c

            } else {

                i = random.nextInt(9)
                result += i.toString()

            }

        }

        return result

    } // getRandomResult()


}