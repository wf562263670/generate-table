package com.generate.mapping;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResultMapping {

    public static Field[] getClassFields(Object object) {
        Class<?> clazz = object.getClass();
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        return fieldList.toArray(fields);
    }

    public static void parseCamelObject(Object object, ResultSet rs) {
        Field[] fields = getClassFields(object);
        String fieldName, type;
        try {
            for (Field field : fields) {
                fieldName = field.getName();
                if ("String".equals(type = field.getType().getSimpleName())) {
                    parseBean(fields, object, fieldName, rs.getString(CamelMapping.parseCamel(fieldName)));
                } else if ("int".equals(type) || "Integer".equals(type)) {
                    parseBean(fields, object, fieldName, rs.getInt(CamelMapping.parseCamel(fieldName)));
                } else if ("long".equals(type) || "Long".equals(type)) {
                    parseBean(fields, object, fieldName, rs.getLong(CamelMapping.parseCamel(fieldName)));
                } else if (("boolean".equals(type) || "Boolean".equals(type)) && ("true".equals(rs.getString(CamelMapping.parseCamel(fieldName))) || "false".equals(rs.getString(CamelMapping.parseCamel(fieldName))))) {
                    parseBean(fields, object, fieldName, Boolean.parseBoolean(rs.getString(CamelMapping.parseCamel(fieldName))));
                } else if ("boolean".equals(type)) {
                    parseBean(fields, object, fieldName, rs.getInt(CamelMapping.parseCamel(fieldName)) > 0);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static <E> List<E> toList(ResultSet rs, Class<E> clazz) {
        List<E> list = new ArrayList<>();
        try {
            while (rs.next()) {
                Object object = clazz.newInstance();
                parseCamelObject(object, rs);
                list.add((E) object);
            }
        } catch (InstantiationException | IllegalAccessException | SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void parseBean(Field[] fields, Object bean, String fieldName, Object value) {
        try {
            String name;
            for (Field field : fields) {
                name = field.getName();
                if (name.equals(fieldName)) {
                    field.setAccessible(true);
                    field.set(bean, value);
                    return;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
