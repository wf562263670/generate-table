package com.generate.mapping;

import java.util.Locale;

/**
 * wangfei
 */
public class CamelMapping {

    private static final String CHARACTER = "_";

    public static String parseString(String value) {
        StringBuilder sb = new StringBuilder(value);
        int index = value.indexOf(CHARACTER);
        if (index < 0) {
            return value;
        }
        if (index > 0 && index < value.length() - 1) {
            String toUpperCase = String.valueOf(value.charAt(index + 1)).toUpperCase();
            sb.replace(index + 1, index + 2, toUpperCase);
        }
        value = sb.replace(index, index + 1, "").toString();
        if (value.contains("_")) {
            return parseString(value);
        }
        return sb.toString();
    }

    public static String toLowerCaseFirstOne(String s){
        if(Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return Character.toLowerCase(s.charAt(0)) + s.substring(1);
    }

    public static String parseCamel(String value) {
        StringBuilder sb = new StringBuilder(value);
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            if (Character.isUpperCase(ch) && i > 0) {
                String toLowerCase = String.valueOf(value.charAt(i)).toLowerCase();
                sb.insert(i, CHARACTER);
                sb.replace(i + 1, i + 2, toLowerCase);
                break;
            }
        }
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            if (Character.isUpperCase(ch) && i > 0) {
                return parseCamel(sb.toString());
            }
        }
        return sb.toString().toLowerCase(Locale.ROOT);
    }
}
