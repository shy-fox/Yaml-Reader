package io.shiromi.saml.tools;

import org.jetbrains.annotations.NotNull;

public abstract class Formatter {
    public static @NotNull String numberFormat(double d, int rightFormat) {
        boolean NEG = d < 0;
        String _N = Double.toString(d);
        String N = NEG ? _N.substring(1) : _N;

        int DI = N.indexOf('.');

        String IP = DI != -1 ? N.substring(0, DI) : N;
        String DP = DI != -1 ? N.substring(DI + 1) : "";
        StringBuilder STR = new StringBuilder(NEG ? "-" : "");

        if (rightFormat > 0) {
            String DEC = "";
            if (DI != -1 || rightFormat - DP.length() > 0) DEC = ".";
            if (DP.length() > rightFormat) DP = DP.substring(0, rightFormat);

            STR.append(IP).append(DEC).append(DP);

            STR.append("0".repeat(rightFormat - DP.length()));
        } else {
            STR.append(IP);
        }

        return STR.toString();
    }

    public static @NotNull String hexFormat(int number, int length) {
        StringBuilder buf = new StringBuilder();
        String hex = toHexString(number);
        while (buf.length() + hex.length() > length) buf.append('0');
        return buf.append(hex).toString();
    }

    private static @NotNull String toHexString(int i) {
        if (i < 0) i *= -1;
        int r;
        StringBuilder h = new StringBuilder();
        char[] hex = {
                '0', '1', '2', '3',
                '4', '5', '6', '7',
                '8', '9', 'A', 'B',
                'C', 'D', 'E', 'F'
        };

        while (i > 0) {
            r = i % 16;
            h.append(hex[r]);
            i /= 16;
        }

        return h.toString();
    }
}
