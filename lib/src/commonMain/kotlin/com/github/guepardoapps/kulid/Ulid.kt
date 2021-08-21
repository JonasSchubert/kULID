/**
 * MIT License
 *
 * Copyright (c) 2019-2021 GuepardoApps (Jonas Schubert)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.guepardoapps.kulid

import kotlinx.datetime.Clock
import kotlin.experimental.and
import kotlin.random.Random

class ULID {
    companion object {
        /**
         * The default entropy size
         */
        private const val DEFAULT_ENTROPY_SIZE = 10

        /**
         * Maximum allowed timestamp value.
         */
        const val TIMESTAMP_MAX = 0x0000ffffffffffffL

        /**
         * Minimum allowed timestamp value.
         */
        const val TIMESTAMP_MIN = 0x0L

        /**
         * ULID string length.
         */
        private const val ULID_LENGTH = 26

        /**
         * Base32 characters mapping
         */
        private val charMapping = charArrayOf(
                0x30.toChar(), 0x31.toChar(), 0x32.toChar(), 0x33.toChar(), 0x34.toChar(), 0x35.toChar(),
                0x36.toChar(), 0x37.toChar(), 0x38.toChar(), 0x39.toChar(), 0x41.toChar(), 0x42.toChar(),
                0x43.toChar(), 0x44.toChar(), 0x45.toChar(), 0x46.toChar(), 0x47.toChar(), 0x48.toChar(),
                0x4a.toChar(), 0x4b.toChar(), 0x4d.toChar(), 0x4e.toChar(), 0x50.toChar(), 0x51.toChar(),
                0x52.toChar(), 0x53.toChar(), 0x54.toChar(), 0x56.toChar(), 0x57.toChar(), 0x58.toChar(),
                0x59.toChar(), 0x5a.toChar()
        )

        /**
         * `char` to `byte` O(1) mapping with alternative chars mapping
         */
        private val charToByteMapping = byteArrayOf(
                0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
                0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
                0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
                0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
                0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
                0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
                0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
                0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
                0x00.toByte(), 0x01.toByte(), 0x02.toByte(), 0x03.toByte(), 0x04.toByte(), 0x05.toByte(),
                0x06.toByte(), 0x07.toByte(), 0x08.toByte(), 0x09.toByte(), 0xff.toByte(), 0xff.toByte(),
                0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0x0a.toByte(),
                0x0b.toByte(), 0x0c.toByte(), 0x0d.toByte(), 0x0e.toByte(), 0x0f.toByte(), 0x10.toByte(),
                0x11.toByte(), 0xff.toByte(), 0x12.toByte(), 0x13.toByte(), 0xff.toByte(), 0x14.toByte(),
                0x15.toByte(), 0xff.toByte(), 0x16.toByte(), 0x17.toByte(), 0x18.toByte(), 0x19.toByte(),
                0x1a.toByte(), 0xff.toByte(), 0x1b.toByte(), 0x1c.toByte(), 0x1d.toByte(), 0x1e.toByte(),
                0x1f.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
                0xff.toByte(), 0x0a.toByte(), 0x0b.toByte(), 0x0c.toByte(), 0x0d.toByte(), 0x0e.toByte(),
                0x0f.toByte(), 0x10.toByte(), 0x11.toByte(), 0xff.toByte(), 0x12.toByte(), 0x13.toByte(),
                0xff.toByte(), 0x14.toByte(), 0x15.toByte(), 0xff.toByte(), 0x16.toByte(), 0x17.toByte(),
                0x18.toByte(), 0x19.toByte(), 0x1a.toByte(), 0xff.toByte(), 0x1b.toByte(), 0x1c.toByte(),
                0x1d.toByte(), 0x1e.toByte(), 0x1f.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
                0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
                0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
                0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
                0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
                0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
                0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
                0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
                0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
                0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
                0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
                0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
                0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
                0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
                0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
                0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
                0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
                0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
                0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
                0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
                0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
                0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
                0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte()
        )

        /**
         * Generate ULID string from provided string
         * @param string         String
         * @return               ULID string
         */
        fun fromString(string: String): String = if (!isValid(string)) { throw IllegalArgumentException("Invalid string value for an ulid") } else { string }

        /**
         * Generate random ULID string using [kotlin.random.Random] instance.
         * @return               ULID string
         */
        fun random(): String = generate(Clock.System.now().toEpochMilliseconds(), Random.nextBytes(DEFAULT_ENTROPY_SIZE))

        /**
         * Generate ULID from Unix epoch timestamp in millisecond and entropy bytes.
         * Throws [kotlin.IllegalArgumentException] if timestamp is less than {@value #MIN_TIME},
         * is more than {@value #MAX_TIME}, or entropy bytes is null or less than 10 bytes.
         * @param time           Unix epoch timestamp in millisecond
         * @param entropy        Entropy bytes
         * @return               ULID string
         */
        fun generate(time: Long, entropy: ByteArray?): String {
            if (time < TIMESTAMP_MIN || time > TIMESTAMP_MAX || entropy == null || entropy.size < DEFAULT_ENTROPY_SIZE) {
                throw IllegalArgumentException("Time is too long, or entropy is less than 10 bytes or null")
            }

            val chars = CharArray(ULID_LENGTH)

            // time
            chars[0] = charMapping[time.ushr(45).toInt() and 0x1f]
            chars[1] = charMapping[time.ushr(40).toInt() and 0x1f]
            chars[2] = charMapping[time.ushr(35).toInt() and 0x1f]
            chars[3] = charMapping[time.ushr(30).toInt() and 0x1f]
            chars[4] = charMapping[time.ushr(25).toInt() and 0x1f]
            chars[5] = charMapping[time.ushr(20).toInt() and 0x1f]
            chars[6] = charMapping[time.ushr(15).toInt() and 0x1f]
            chars[7] = charMapping[time.ushr(10).toInt() and 0x1f]
            chars[8] = charMapping[time.ushr(5).toInt() and 0x1f]
            chars[9] = charMapping[time.toInt() and 0x1f]

            // entropy
            chars[10] = charMapping[(entropy[0].toShort() and 0xff).toInt().ushr(3)]
            chars[11] = charMapping[(entropy[0].toInt() shl 2 or (entropy[1].toShort() and 0xff).toInt().ushr(6) and 0x1f)]
            chars[12] = charMapping[((entropy[1].toShort() and 0xff).toInt().ushr(1) and 0x1f)]
            chars[13] = charMapping[(entropy[1].toInt() shl 4 or (entropy[2].toShort() and 0xff).toInt().ushr(4) and 0x1f)]
            chars[14] = charMapping[(entropy[2].toInt() shl 5 or (entropy[3].toShort() and 0xff).toInt().ushr(7) and 0x1f)]
            chars[15] = charMapping[((entropy[3].toShort() and 0xff).toInt().ushr(2) and 0x1f)]
            chars[16] = charMapping[(entropy[3].toInt() shl 3 or (entropy[4].toShort() and 0xff).toInt().ushr(5) and 0x1f)]
            chars[17] = charMapping[(entropy[4].toInt() and 0x1f)]
            chars[18] = charMapping[(entropy[5].toShort() and 0xff).toInt().ushr(3)]
            chars[19] = charMapping[(entropy[5].toInt() shl 2 or (entropy[6].toShort() and 0xff).toInt().ushr(6) and 0x1f)]
            chars[20] = charMapping[((entropy[6].toShort() and 0xff).toInt().ushr(1) and 0x1f)]
            chars[21] = charMapping[(entropy[6].toInt() shl 4 or (entropy[7].toShort() and 0xff).toInt().ushr(4) and 0x1f)]
            chars[22] = charMapping[(entropy[7].toInt() shl 5 or (entropy[8].toShort() and 0xff).toInt().ushr(7) and 0x1f)]
            chars[23] = charMapping[((entropy[8].toShort() and 0xff).toInt().ushr(2) and 0x1f)]
            chars[24] = charMapping[(entropy[8].toInt() shl 3 or (entropy[9].toShort() and 0xff).toInt().ushr(5) and 0x1f)]
            chars[25] = charMapping[(entropy[9].toInt() and 0x1f)]

            return chars.concatToString()
        }

        /**
         * Checks ULID string validity.
         * @param ulid           ULID nullable string
         * @return               true if ULID string is valid
         */
        fun isValid(ulid: String?): Boolean {
            if (ulid == null || ulid.length != ULID_LENGTH) {
                return false
            }

            for (char in ulid) {
                if (char.code < 0
                        || char.code > charToByteMapping.size
                        || charToByteMapping[char.code] == 0xff.toByte()) {
                    return false
                }
            }

            return true
        }

        /**
         * Extract and return the timestamp part from ULID. Expects a valid ULID string.
         * Call [ULID.isValid] and check validity before calling this method if you
         * do not trust the origin of the ULID string.
         * @param ulid           ULID string
         * @return               Unix epoch timestamp in millisecond
         */
        fun getTimestamp(ulid: CharSequence): Long {
            return (charToByteMapping[ulid[0].code].toLong() shl 45
                    or (charToByteMapping[ulid[1].code].toLong() shl 40)
                    or (charToByteMapping[ulid[2].code].toLong() shl 35)
                    or (charToByteMapping[ulid[3].code].toLong() shl 30)
                    or (charToByteMapping[ulid[4].code].toLong() shl 25)
                    or (charToByteMapping[ulid[5].code].toLong() shl 20)
                    or (charToByteMapping[ulid[6].code].toLong() shl 15)
                    or (charToByteMapping[ulid[7].code].toLong() shl 10)
                    or (charToByteMapping[ulid[8].code].toLong() shl 5)
                    or charToByteMapping[ulid[9].code].toLong())
        }

        /**
         * Extract and return the entropy part from ULID. Expects a valid ULID string.
         * Call [ULID.isValid] and check validity before calling this method if you
         * do not trust the origin of the ULID string.
         * @param ulid           ULID string
         * @return               Entropy bytes
         */
        fun getEntropy(ulid: CharSequence): ByteArray {
            val bytes = ByteArray(DEFAULT_ENTROPY_SIZE)

            bytes[0] = (charToByteMapping[ulid[10].code].toInt() shl 3
                    or (charToByteMapping[ulid[11].code] and 0xff.toByte()).toInt().ushr(2)).toByte()
            bytes[1] = (charToByteMapping[ulid[11].code].toInt() shl 6
                    or (charToByteMapping[ulid[12].code].toInt() shl 1)
                    or (charToByteMapping[ulid[13].code] and 0xff.toByte()).toInt().ushr(4)).toByte()
            bytes[2] = (charToByteMapping[ulid[13].code].toInt() shl 4
                    or (charToByteMapping[ulid[14].code] and 0xff.toByte()).toInt().ushr(1)).toByte()
            bytes[3] = (charToByteMapping[ulid[14].code].toInt() shl 7
                    or (charToByteMapping[ulid[15].code].toInt() shl 2)
                    or (charToByteMapping[ulid[16].code] and 0xff.toByte()).toInt().ushr(3)).toByte()
            bytes[4] = (charToByteMapping[ulid[16].code].toInt() shl 5
                    or (charToByteMapping[ulid[17].code].toInt())).toByte()
            bytes[5] = (charToByteMapping[ulid[18].code].toInt() shl 3
                    or (charToByteMapping[ulid[19].code] and 0xff.toByte()).toInt().ushr(2)).toByte()
            bytes[6] = (charToByteMapping[ulid[19].code].toInt() shl 6
                    or (charToByteMapping[ulid[20].code].toInt() shl 1)
                    or (charToByteMapping[ulid[21].code] and 0xff.toByte()).toInt().ushr(4)).toByte()
            bytes[7] = (charToByteMapping[ulid[21].code].toInt() shl 4
                    or (charToByteMapping[ulid[22].code] and 0xff.toByte()).toInt().ushr(1)).toByte()
            bytes[8] = (charToByteMapping[ulid[22].code].toInt() shl 7
                    or (charToByteMapping[ulid[23].code].toInt() shl 2)
                    or (charToByteMapping[ulid[24].code] and 0xff.toByte()).toInt().ushr(3)).toByte()
            bytes[9] = (charToByteMapping[ulid[24].code].toInt() shl 5
                    or (charToByteMapping[ulid[25].code].toInt())).toByte()

            return bytes
        }
    }
}