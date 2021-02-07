package com.generate.config;

import java.util.Arrays;

public enum Type {

    //字符串
    CHAR, VARCHAR,

    //整数
    TINYINT, INT, BIGINT,

    //小数
    FLOAT, DOUBLE, DECIMAL,

    //日期
    DATE, DATETIME, TIMESTAMP,

    //其他类型
    BLOB, TEXT;

    public enum string {
        CHAR, VARCHAR
    }

    public enum number {
        TINYINT, INT, BIGINT
    }

    public enum decimal {
        FLOAT, DOUBLE, DECIMAL
    }

    public enum time {
        DATE, DATETIME, TIMESTAMP
    }

    public enum other {
        BLOB, TEXT
    }

    public static boolean isString(String type) {
        return Arrays.toString(string.values()).contains(type);
    }

    public static boolean isNumber(String type) {
        return Arrays.toString(number.values()).contains(type);
    }

    public static boolean isDecimal(String type) {
        return Arrays.toString(decimal.values()).contains(type);
    }

    public static boolean isTime(String type) {
        return Arrays.toString(time.values()).contains(type);
    }

}
