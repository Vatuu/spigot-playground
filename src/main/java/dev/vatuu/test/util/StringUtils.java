package dev.vatuu.test.util;

import java.util.Map;

public class StringUtils {

    @SafeVarargs
    public static String formatString(String mask, Tuple<String, String>... values) {
        String s = mask;
        for (Tuple<String, String> e : values)
            s = s.replace(e.getLeft(), e.getRight());
        return s;
    }
}
