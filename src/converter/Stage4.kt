package converter

import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode
import kotlin.system.exitProcess

fun main() {
    converter()

}
private fun Char.HEX() = when (this) {
    '0' -> 0
    '1' -> 1
    '2' -> 2
    '3' -> 3
    '4' -> 4
    '5' -> 5
    '6' -> 6
    '7' -> 7
    '8' -> 8
    '9' -> 9
    'a', 'A' -> 10
    'b', 'B' -> 11
    'c', 'C' -> 12
    'd', 'D' -> 13
    'e', 'E' -> 14
    'f', 'F' -> 15
    'g', 'G' -> 16
    'h', 'H' -> 17
    'i', 'I' -> 18
    'j', 'J' -> 19
    'k', 'K' -> 20
    'l', 'L' -> 21
    'm', 'M' -> 22
    'n', 'N' -> 23
    'o', 'O' -> 24
    'p', 'P' -> 25
    'q', 'Q' -> 26
    'r', 'R' -> 27
    's', 'S' -> 28
    't', 'T' -> 29
    'u', 'U' -> 30
    'v', 'V' -> 31
    'w', 'W' -> 32
    'x', 'X' -> 33
    'y', 'Y' -> 34
    'z', 'Z' -> 35
    else -> 0
}

private fun converter() {
    print("Enter two numbers in format: {source base} {target base} (To quit type /exit) ")
    val input = readln()
    if (input == "/exit") {
        exitProcess(0)
    }else if (input.isEmpty()){
        println("Please enter a valid number")
        return converter()
    } else {
        var (source, base) = input.split(" ").map { it.toBigInteger() }
        do {
            print("Enter number in base $source to convert to base $base (To go back type /back) ")
            var num = readln()
            if (num == "/back") {
                return converter()

            } else if (num.contains(".") && base.toInt() == 10 && source in 2.toBigInteger()..36.toBigInteger()) {
                fractionToDecimal(num, source)

            } else if (num.contains(".") && base in 2.toBigInteger()..36.toBigInteger()
                && source in 2.toBigInteger()..36.toBigInteger() && base.toInt() != 10
            ) {
                anyFractionsToAnyBase(num, base, source)
            } else if (base == 10.toBigInteger() && source in 2.toBigInteger()..36.toBigInteger()) {
                basesToDecimal(num, source)
            } else if (base in 2.toBigInteger()..36.toBigInteger() && source in 2.toBigInteger()..36.toBigInteger() && base.toInt() != 10) {
                baseToBase(num, base, source)

            } else if (base !in 2.toBigInteger()..36.toBigInteger() || base == null) {
                println("$base is not a valid base")
            } else if (source !in 2.toBigInteger()..36.toBigInteger() || source == null) {
                println("$source is not a valid base")
            }

        } while (num != "/back")
    }
}

private fun basesToDecimal(num: String, source: BigInteger) {
    if (num.isEmpty() || num.any { it.HEX().toBigInteger() >= source }) {
        println("Sorry cannot convert $num from base $source to a decimal \n")
    } else if (num.isNotEmpty() && num.all { it.isLetterOrDigit() }) {
        var decResult = BigInteger.ZERO
        var count = num.count() - 1
        for (i in num.indices) {
            decResult += num[i].HEX().toBigInteger() * source.pow(count)
            count--
        }
        println("Conversion result: $decResult \n")
    } else if (num.contains("-")) {
        var negResult = BigInteger.ZERO
        var count = num.count() - 2
        for (i in num.indices) {
            if (num[i] == '-') continue
            negResult += -(num[i].HEX().toBigInteger() * source.pow(count))
            count--
        }
        println("Conversion result: $negResult \n")
    }

}

private fun baseToBase(num: String, base: BigInteger, source: BigInteger) {
    var decResult = BigInteger.ZERO
    var count = num.count() - 1

    for (i in num.indices) {
        decResult += (num[i].HEX().toBigInteger() * source.pow(count))
        count--
    }
    if (num.isEmpty() || num.any { it.HEX().toBigInteger() >= source }) {
        println("Sorry cannot convert $num from base $source to base $base\n")

    } else if (num.isNotEmpty() && num.all { it.isLetterOrDigit() }) {
        val Hexa = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        var baseResult = ""
        do {
            baseResult += Hexa[(decResult % base).toInt()]
            decResult /= base
        } while (decResult > BigInteger.ZERO)

        println("Conversion result: ${baseResult.reversed()}")

    } else if (num.contains("-")) {
        val Hexas = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        var negBaseResult = ""
        do {
            negBaseResult += (Hexas[(decResult % base).toInt()])
            decResult /= base
        } while (decResult > BigInteger.ZERO)

        println("Conversion result: -${negBaseResult.reversed()}")
    }

}

private fun fractionToDecimal(num: String, source: BigInteger) {
    var (integer, fraction) = num.split(".")

    if (num.isEmpty() || num.any { it.HEX().toBigDecimal() >= source.toBigDecimal() }) {
        println("Cannot convert $num from base $source to decimal\n")

    } else if (num[0] != '-') {
        var intRes = BigInteger.ZERO
        var intCount = integer.count() - 1
        for (i in integer.indices) {
            intRes += (integer[i].HEX().toBigInteger() * source.pow(intCount))
            intCount--
        }

        var fracRes = BigDecimal.ZERO
        var fracCount = fraction.count()
        for (i in fraction.indices) {
            fracRes += (fraction[i].HEX().toBigDecimal() * source.toBigDecimal().pow(-(i + 1), MathContext.DECIMAL128))
            fracCount--
        }
        println("Conversion result: ${(intRes.toBigDecimal() + fracRes).setScale(5, RoundingMode.HALF_UP)} \n")

    }
    if (num.contains("-")) {
        var negintRes = BigInteger.ZERO
        var intCount = integer.count() - 1
        for (i in integer.indices) {
            negintRes += (integer[i].HEX().toBigInteger() * source.pow(intCount))
            intCount--
        }

        var negfracRes = BigDecimal.ZERO
        var fracCount = fraction.count()
        for (i in fraction.indices) {
            negfracRes += (fraction[i].HEX().toBigDecimal() * source.toBigDecimal()
                .pow(-(i + 1), MathContext.DECIMAL64))// .toBigDecimal()
            fracCount--
        }
        println("Conversion result: -${(negintRes.toBigDecimal() + negfracRes).setScale(5, RoundingMode.HALF_UP)} \n")
    }


}

private fun anyFractionsToAnyBase(num: String, base: BigInteger, source: BigInteger) {
    var (integer, fraction) = num.split(".")

    var intRes = BigInteger.ZERO
    var intCount = integer.count() - 1
    for (i in integer.indices) {
        intRes += (integer[i].HEX().toBigInteger() * source.pow(intCount))
        intCount--
    }

    var fracRes = BigDecimal.ZERO
    var fracCount = fraction.count()
    for (i in fraction.indices) {
        fracRes += (fraction[i].HEX().toBigDecimal() * source.toBigDecimal().pow(-(i + 1), MathContext.DECIMAL128))
        fracCount--
    }

    if (num.isEmpty() || num.any { it.HEX().toBigDecimal() >= source.toBigDecimal() }) {
        println("Cannot convert $num from base $source to base $base\n")

    } else if (num[0] != '-') {
        val hexa = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        var intbaseResult = ""
        do {
            intbaseResult += hexa[(intRes % base).toInt()]
            intRes /= base
        } while (intRes > BigInteger.ZERO)

        var fracbaseResult = ""
        var num = fracRes
        var frac: BigDecimal
        do {
            for (i in 1..5) {
                num *= base.toBigDecimal()
                frac = num.remainder(BigDecimal.ONE)
                fracbaseResult += hexa[num.toInt()]
                num = frac
            }
        } while (num <= BigDecimal.ONE && fracbaseResult.length < 5)
        println("Conversion result: ${(intbaseResult.reversed())}.${(fracbaseResult)}\n")
    }


    if (num[0] == '-') {
        val hexa = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        var intbaseResult = ""
        do {
            intbaseResult += hexa[(intRes % base).toInt()]
            intRes /= base
        } while (intRes > BigInteger.ZERO)

        var fracbaseResult = ""
        var num = fracRes
        var frac: BigDecimal
        do {
            for (i in 1..5) {
                num *= base.toBigDecimal()
                frac = num.remainder(BigDecimal.ONE)
                fracbaseResult += hexa[num.toInt()]
                num = frac
            }
        } while (num <= BigDecimal.ONE && fracbaseResult.length < 5)
        println("Conversion result: -${(intbaseResult.reversed())}.${(fracbaseResult)}\n")
    }

}





