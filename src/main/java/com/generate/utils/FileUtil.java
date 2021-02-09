package com.generate.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class FileUtil {

    private static class read {

        private static final Map<Object, Object> data = new HashMap<>();

        private static Map<Object, Object> config() {
            if (!data.isEmpty()) return data;
            Properties properties = FileUtil.load("application.properties");
            Set<Map.Entry<Object, Object>> entries = properties.entrySet();
            String key, value, k;
            for (Map.Entry<Object, Object> entry : entries) {
                key = entry.getKey().toString();
                value = entry.getValue().toString();
                if (value.startsWith("$") && value.endsWith("}")) {
                    k = value.substring(2, value.length() - 1);
                    for (Map.Entry<Object, Object> e : entries) {
                        if (e.getKey().toString().equals(k)) {
                            value = e.getValue().toString();
                        }
                    }
                }
                data.put(key, value);
                System.out.println(key + "---" + value);
            }
            return data;
        }

    }

    public static void main(String[] args) {
        getConfig();
    }


    private FileUtil() {
    }

    public static Map<Object, Object> getConfig() {
        return read.config();
    }

    //    读取配置文件
    public static Properties load(String path) {
        Properties properties = new Properties();
        try (InputStream inputStream = FileUtil.class.getClassLoader().getResourceAsStream(path)) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

}
