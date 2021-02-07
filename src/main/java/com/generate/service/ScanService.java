package com.generate.service;

import com.generate.annotation.Column;
import com.generate.annotation.Table;
import com.generate.config.AutoTable;
import com.generate.mapping.CamelMapping;
import com.generate.utils.FileUtil;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

public class ScanService {

    private static List<String> classList = new ArrayList<>();

    private static Map<String, List<AutoTable>> map = new HashMap();

    private void scanPackage(String path) {
        path = path.replace(".", "/");
        String url = Objects.requireNonNull(this.getClass().getClassLoader().getResource(path)).getFile();
        File file = new File(url);
        String[] list = file.list();
        if (list == null || list.length < 1) return;
        for (String filePath : list) {
            file = new File(url + "/" + filePath);
            if (file.isDirectory()) {
                scanPackage(path + "/" + filePath);
            } else {
                String className = path + "." + file.getName();
                className = className.replace("/", ".");
                classList.add(className);
            }
        }
    }

    private void getTable() {
        Map<Object, Object> params = FileUtil.getConfig();
        String path = params.get("table.entity.path").toString();
        scanPackage(path);
        if (classList.size() < 1) return;
        String tableName;
        AutoTable autoTable;
        Class<?> clazz;
        Table table;
        try {
            for (String className : classList) {
                className = className.replace(".class", "");
                clazz = Class.forName(className);
                if (!clazz.isAnnotationPresent(Table.class)) continue;
                table = clazz.getAnnotation(Table.class);
                tableName = "".equals(table.name()) ? CamelMapping.parseCamel(clazz.getSimpleName()).toLowerCase() : table.name();
                autoTable = new AutoTable();
                autoTable.setTableName(tableName);
                autoTable.setTableRemark(table.remark());
                getField(clazz, autoTable);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void getField(Class<?> clazz, AutoTable autoTable) {
        Field[] fields = clazz.getDeclaredFields();
        if (fields.length < 1) return;
        Column column;
        String fieldName;
        AutoTable a;
        List<AutoTable> tableList = new ArrayList<>();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Column.class)) continue;
            column = field.getAnnotation(Column.class);
            a = new AutoTable();
            fieldName = "".equals(column.name()) ? CamelMapping.parseCamel(field.getName()) : column.name();
            a.setTableName(autoTable.getTableName());
            a.setTableRemark(autoTable.getTableRemark());
            a.setField(field.getName());
            a.setFieldName(fieldName);
            a.setType(column.type().toString());
            a.setSize(column.size());
            a.setDecimals(column.decimal());
            a.setFieldRemark(column.remark());
            a.setKey(column.isKey());
            a.setNull(column.isNull());
            a.setDefaults(column.defaults());
            a.setAutoIncrement(column.isAutoIncrement());
            tableList.add(a);
        }
        map.put(autoTable.getTableName(), tableList);
    }

        private static class instance {
            private static Map<String, List<AutoTable>> tableData() {
                if (!map.isEmpty()) return map;
                ScanService scanService = new ScanService();
                scanService.getTable();
                return map;
            }
        }

    private ScanService() {
    }

    public static Map<String, List<AutoTable>> getTableData() {
        return instance.tableData();
    }

}
