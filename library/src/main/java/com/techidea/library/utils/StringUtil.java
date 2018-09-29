package com.techidea.library.utils;

import java.math.BigDecimal;

/**
 * Created by sam on 2018/1/25.
 */

public class StringUtil {

    public static boolean isEmpty(CharSequence string) {
        return (string == null
                || string.equals("null")
                || string.equals("NULL")
                || string.toString().trim().length() <= 0);
    }

    public static String formatCNY(String money) {
        String content = "0";
        if (!isEmpty(money)) {
            content = money;
        }
        content = roundTwo(content);
        content = "￥" + content;
        return content;
    }

    public static String roundTwo(String v) {
        BigDecimal b;
        try {
            b = new BigDecimal(v);
        } catch (NumberFormatException e) {
            b = new BigDecimal("0");
        }
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, 2, BigDecimal.ROUND_HALF_UP).toString();
    }
}
