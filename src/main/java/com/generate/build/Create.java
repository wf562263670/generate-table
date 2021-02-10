package com.generate.build;

import com.generate.config.AutoTable;
import com.generate.config.Type;
import com.generate.service.ExecuteService;
import com.generate.service.ScanService;
import com.generate.utils.DBConnection;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class Create {

    private final DBConnection conn = new DBConnection();
    private final List<String> table = ExecuteService.getTable();
    private final ExecuteService executeService = new ExecuteService();
    private final Map<String, List<AutoTable>> tableData = ScanService.getTableData();

    public void create(ExecutorService executorService) {
        createBaseTable();
        for (Map.Entry<String, List<AutoTable>> entry : tableData.entrySet()) {
//            executorService.execute(() -> {
            String tableName = entry.getKey();
            if (!table.contains(tableName)) {
                List<AutoTable> list = entry.getValue();
                createSQL(tableName, list);
            }
//            });
        }
    }

    public void createSQL(String tableName, List<AutoTable> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE `").append(tableName).append("` (");
        AutoTable field;
        String type;
        for (int i = 0, length = list.size(); i < length; i++) {
            field = list.get(i);
            type = field.getType();
            sb.append(field.getFieldName()).append(" ").append(type);
            if (Type.isString(type) || Type.isNumber(type)) {
                sb.append("(").append(field.getSize()).append(") ");
            } else if (Type.isDecimal(type)) {
                sb.append("(").append(field.getSize()).append(",").append(field.getDecimals()).append(") ");
            } else if (type.equals("TIMESTAMP")) {
                if ("create_time".equals(field.getFieldName())) sb.append(" DEFAULT CURRENT_TIMESTAMP ");
                if ("update_time".equals(field.getFieldName()))
                    sb.append(" DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ");
            } else {
                sb.append(" ");
            }
            if (!field.isNull()) sb.append("NOT NULL ");
            if (field.isKey()) sb.append("PRIMARY KEY ");
            if (field.isAutoIncrement() && Type.isNumber(type)) sb.append("AUTO_INCREMENT ");
            if (!"".equals(field.getDefaults())) sb.append("DEFAULT '").append(field.getDefaults()).append("' ");
            if (!"".equals(field.getFieldRemark())) sb.append("COMMENT '").append(field.getFieldRemark()).append("'");
            if (i < length - 1) sb.append(",");
        }
        sb.append(") CHARACTER SET utf8 COLLATE utf8_general_ci");
        executeService.deleteTable(tableName);
        conn.execute(sb.toString());
        executeService.addTableData(list);
    }

    public void createBaseTable() {
        if (!table.contains("auto_table")) {
            String sql = "CREATE TABLE `auto_table` (table_name VARCHAR(40) NOT NULL COMMENT '表名称',table_remark VARCHAR(40) COMMENT '表备注',field VARCHAR(40) NOT NULL COMMENT '对象字段',field_name VARCHAR(40) NOT NULL COMMENT '表字段',type VARCHAR(10) NOT NULL COMMENT '类型',size INT(5) NOT NULL COMMENT '长度',decimals VARCHAR(5) NOT NULL COMMENT '小数长度',defaults VARCHAR(40) COMMENT '默认值',is_key VARCHAR(5) NOT NULL COMMENT '是否为主键',is_null VARCHAR(5) NOT NULL COMMENT '是否为空',is_auto_increment VARCHAR(5) NOT NULL COMMENT '是否自增',field_remark VARCHAR(40) COMMENT '字段备注') CHARACTER SET utf8 COLLATE utf8_general_ci";
            conn.execute(sql);
        }
        tableData.remove("auto_table");
    }

}
