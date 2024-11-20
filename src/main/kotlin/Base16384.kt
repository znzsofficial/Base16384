import kotlin.math.ceil
import kotlin.math.floor

/**
 * Base16384 编解码实现
 *
 * 本代码实现了 Base16384 编解码算法，将二进制数据编码为 Unicode 字符并解码回原始数据。
 * 此实现基于 TypeScript 库 [base16384.js](https://github.com/shigma/base16384.js) 进行改编。
 *
 * 原库作者: [shigma](https://github.com/shigma)
 * 原库地址: https://github.com/shigma/base16384.js
 *
 * 本代码遵循 MIT 开源许可证，与原库保持一致。
 * 如需了解更多，请参考原始实现和其许可证文件。
 */


object Base16384 {
    private const val SOURCE_WIDTH = 8
    private const val TARGET_WIDTH = 14
    private const val SOURCE_OFFSET = 0
    private const val TARGET_OFFSET = 0x4E00
    private const val TERMINATOR_OFFSET = 0x3D00

    private fun align(
        input: ByteArray,
        output: ShortArray,
        sWidth: Int,
        tWidth: Int,
        sOffset: Int,
        tOffset: Int
    ) {
        var offset = 0
        var rest = 0
        var i = 0
        var j = 0
        val mask = (1 shl tWidth) - 1
        while (i < input.size) {
            val char = (input[i].toInt() and 0xFF) - sOffset
            offset += sWidth
            while (offset >= tWidth) {
                offset -= tWidth
                output[j++] = (rest + (char shr offset) + tOffset).toShort()
                if (j == output.size) return
                rest = 0
            }
            rest += (char shl (tWidth - offset)) and mask
            i++
        }
        if (offset > 0) {
            output[j] = (rest + tOffset).toShort()
        }
    }

    private fun align(
        input: ShortArray,
        output: ByteArray,
        sWidth: Int,
        tWidth: Int,
        sOffset: Int,
        tOffset: Int
    ) {
        var offset = 0
        var rest = 0
        var i = 0
        var j = 0
        val mask = (1 shl tWidth) - 1
        while (i < input.size) {
            val char = (input[i].toInt() and 0xFFFF) - sOffset
            offset += sWidth
            while (offset >= tWidth) {
                offset -= tWidth
                output[j++] = (rest + (char shr offset) + tOffset).toByte()
                if (j == output.size) return
                rest = 0
            }
            rest += (char shl (tWidth - offset)) and mask
            i++
        }
        if (offset > 0) {
            output[j] = (rest + tOffset).toByte()
        }
    }

    fun encode(input: String): ShortArray {
        val byteInput = input.toByteArray(Charsets.UTF_8)
        val output = ShortArray(ceil(byteInput.size * 4 / 7.0).toInt() + 1)
        align(byteInput, output, SOURCE_WIDTH, TARGET_WIDTH, SOURCE_OFFSET, TARGET_OFFSET)
        output[output.lastIndex] = ((byteInput.size % 7) + TERMINATOR_OFFSET).toShort()
        return output
    }

    fun decode(input: ShortArray): ByteArray {
        val length = input.size - 1
        val residue = (input[length].toInt() and 0xFFFF) - TERMINATOR_OFFSET
        val effectiveResidue = if (residue == 0) 7 else residue
        val output = ByteArray(floor((length - 1) / 4.0).toInt() * 7 + effectiveResidue)
        align(input, output, TARGET_WIDTH, SOURCE_WIDTH, TARGET_OFFSET, SOURCE_OFFSET)
        return output
    }

    fun encodeToString(input: String): String {
        val encodedArray = encode(input)
        return encodedArray.joinToString("") { it.toChar().toString() }
    }

    fun decodeFromString(input: String): String {
        val shortArray = input.map { it.code.toShort() }.toShortArray()
        val decodedBytes = decode(shortArray)
        return String(decodedBytes, Charsets.UTF_8)
    }
}