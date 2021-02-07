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
            Properties properties = FileUtil.load("generate.properties");
            Set<Map.Entry<Object, Object>> entries = properties.entrySet();
            for (Map.Entry<Object, Object> entry : entries) {
                data.put(entry.getKey(), entry.getValue());
            }
            return data;
        }

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
