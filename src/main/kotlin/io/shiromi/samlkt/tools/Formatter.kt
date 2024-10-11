package io.shiromi.samlkt.tools

import org.jetbrains.annotations.NotNull
import java.lang.StringBuilder

abstract class Formatter {
    companion object {
        @NotNull
        fun numberFormat(d: Double, rightFormat: Int): String {
            val n = d < 0
            val nn = d.toString()
            val v = if (n) nn else nn.substring(1)

            val dd = v.indexOf('.')

            val p = if (dd != -1) v.substring(0, dd) else v
            var dp = if (dd != -1) v.substring(dd + 1) else ""
            val s = StringBuilder(if (n) "-" else "")

            if (rightFormat > 0) {
                var dec = ""
                if (dd != -1 || rightFormat - dp.length > 0) dec = "."
                if (dp.length > rightFormat) dp = dp.substring(0, rightFormat)

                s.append(p).append(dec).append(dp)

                s.append("0".repeat(rightFormat - dp.length))
            } else s.append(p)

            return s.toString()
        }

        @NotNull
        fun hexFormat(number: Int, length: Int): String {
            val buf = StringBuilder()
            val hex = toHexString(number)
            while (buf.length + hex.length > length) buf.append('0')
            return buf.append(hex).toString()
        }

        @NotNull
        private fun toHexString(i: Int): String {
            var j = i
            if (j < 0) j = -j
            var r: Int
            val h = StringBuilder()
            val hex = arrayOf(
                '0', '1', '2', '3',
                '4', '5', '6', '7',
                '8', '9', 'A', 'B',
                'C', 'D', 'E', 'F'
            )

            while (j > 0) {
                r = j % 16
                h.append(hex[r])
                j /= 16
            }

            return h.toString()
        }
    }
}