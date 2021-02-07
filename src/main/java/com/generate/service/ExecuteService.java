package com.generate.service;



import com.generate.config.AutoTable;
import com.generate.mapping.ResultMapping;
import com.generate.utils.DBConnection;
import com.generate.utils.FileUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExecuteService {

    private final DBConnection conn = new DBConnection();
    private final static Map<Object, Object> params = FileUtil.getConfig();

    public void addTableData(List<AutoTable> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO `auto_table` (table_name,table_remark,field,field_name,type,size,`decimals`,defaults,is_key,is_null,is_auto_increment,field_remark) VALUE ");
        AutoTable autoTable;
        for (int i = 0, length = list.size(); i < length; i++) {
            autoTable = list.get(i);
            sb.append("('").append(autoTable.getTableName()).append("','")
                    .append(autoTable.getTableRemark()).append("','")
                    .append(autoTable.getField()).append("','")
                    .append(autoTable.getFieldName()).append("','")
                    .append(autoTable.getType()).append("','")
                    .append(autoTable.getSize()).append("','")
                    .append(autoTable.getDecimals()).append("','")
                    .append(autoTable.getDefaults()).append("','")
                    .append(autoTable.isKey()).append("','")
                    .append(autoTable.isNull()).append("','")
                    .append(autoTable.isAutoIncrement()).append("','")
                    .append(autoTable.getFieldRemark()).append("')");
            if (i < length - 1) sb.append(",");
        }
        conn.execute(sb.toString());
    }

    public void addData(AutoTable autoTable) {
        String sql = "INSERT INTO `auto_table` (table_name,table_remark,field,field_name,type,size,`decimals`,defaults,is_key,is_null,is_auto_increment,field_remark) VALUE " +
                "('" + autoTable.getTableName() + "','" +
                autoTable.getTableRemark() + "','" +
                autoTable.getField() + "','" +
                autoTable.getFieldName() + "','" +
                autoTable.getType() + "','" +
                autoTable.getSize() + "','" +
                autoTable.getDecimals() + "','" +
                autoTable.getDefaults() + "','" +
                autoTable.isKey() + "','" +
                autoTable.isNull() + "','" +
                autoTable.isAutoIncrement() + "','" +
                autoTable.getFieldRemark() + "')";
        conn.execute(sql);
    }

    public void deleteData(AutoTable autoTable) {
        String sql = "DELETE FROM `auto_table` WHERE table_name = '" + autoTable.getTableName() + "' AND field = '" + autoTable.getField() + "'";
        conn.execute(sql);
    }


    private String getDatabase() {
        String db = params.get("jdbc.url").toString();
        db = db.substring(0, db.indexOf("?") + 1);
        db = db.substring(db.lastIndexOf("/") + 1, db.indexOf("?"));
        return db;
    }

    public Map<String, List<AutoTable>> getTableData() {
        String sql = "SELECT * FROM `auto_table`";
        ResultSet rs = conn.executeQuery(sql);
        List<AutoTable> autoTables = ResultMapping.toList(rs, AutoTable.class);
        if (autoTables.isEmpty()) return null;
        String tableName;
        List<AutoTable> list;
        Map<String, List<AutoTable>> data = new HashMap<>();
        for (AutoTable dbField : autoTables) {
            tableName = dbField.getTableName();
            if (!data.containsKey(tableName)) data.put(tableName, new ArrayList<>());
            list = data.get(tableName);
            list.add(dbField);
        }
        return data;
    }

    public List<String> getTableList() {
        try {
            String sql = "SELECT table_name AS count FROM information_schema.tables WHERE table_schema = '" + getDatabase() + "'";
            List<String> list = new ArrayList<>();
            ResultSet rs = conn.executeQuery(sql);
            while (rs.next()) {
                list.add(rs.getString("count"));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class table {

        private static List<String> data = new ArrayList<>();

        private static List<String> data() {
            if (!data.isEmpty()) return data;
            ExecuteService executeService = new ExecuteService();
            return data = executeService.getTableList();
        }

    }

    private static class tableData {

        private static Map<String, List<AutoTable>> data = new HashMap<>();

        private static Map<String, List<AutoTable>> data() {
            ExecuteService executeService = new ExecuteService();
            return data = executeService.getTableData();
        }

    }

    public static List<String> getTable() {
        return table.data();
    }

    public static Map<String, List<AutoTable>> tableData() {
        return tableData.data();
    }
}
