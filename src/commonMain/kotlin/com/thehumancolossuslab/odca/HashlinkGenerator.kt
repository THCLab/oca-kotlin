package com.thehumancolossuslab.odca

import com.ionspin.kotlin.bignum.integer.toBigInteger
import com.soywiz.krypto.SHA256

@ExperimentalUnsignedTypes
object HashlinkGenerator {
    @UseExperimental(ExperimentalStdlibApi::class)
    fun call(input: String): String {
        val hex = SHA256.digest(
            input.encodeToByteArray()
        ).joinToString(separator = "") {
            it.toInt().and(0xff).toString(16).padStart(2, '0')
        }.toBigInteger(16).toByteArray()

        val link = ByteArray(hex.size, { int -> hex[int] }).encodeToBase58String()
        return "hl:$link"
    }

    private fun ByteArray.encodeToBase58String(): String {
        val alphabet = "123456789abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ"
        val ENCODED_ZERO = alphabet[0]

        val input = copyOf(size)
        if (input.isEmpty()) {
            return ""
        }
        var zeros = 0
        while (zeros < input.size && input[zeros].toInt() == 0) {
            ++zeros
        }
        val encoded = CharArray(input.size * 2)
        var outputStart = encoded.size
        var inputStart = zeros
        while (inputStart < input.size) {
            encoded[--outputStart] = alphabet[divmod(input, inputStart.toUInt(), 256.toUInt(), 58.toUInt()).toInt()]
            if (input[inputStart].toInt() == 0) {
                ++inputStart
            }
        }
        while (outputStart < encoded.size && encoded[outputStart] == ENCODED_ZERO) {
            ++outputStart
        }
        return String(encoded, outputStart, encoded.size - outputStart)
    }

    private fun divmod(number: ByteArray, firstDigit: UInt, base: UInt, divisor: UInt): UInt {
        var remainder = 0.toUInt()
        for (i in firstDigit until number.size.toUInt()) {
            val digit = number[i.toInt()].toUByte()
            val temp = remainder * base + digit
            number[i.toInt()] = (temp / divisor).toByte()
            remainder = temp % divisor
        }
        return remainder
    }
}
