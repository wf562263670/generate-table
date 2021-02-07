package com.generate.build;

import com.generate.config.AutoTable;
import com.generate.config.Type;
import com.generate.mapping.CamelMapping;
import com.generate.service.ExecuteService;
import com.generate.service.ScanService;
import com.generate.utils.DBConnection;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class Update {

    private final DBConnection conn = new DBConnection();
    private final ExecuteService executeService = new ExecuteService();
    private final Map<String, List<AutoTable>> tableData = ScanService.getTableData();
    private final Map<String, List<AutoTable>> data = ExecuteService.tableData();

    public void update(ExecutorService executorService) {
        for (Map.Entry<String, List<AutoTable>> entry : tableData.entrySet()) {
//            executorService.execute(() -> {
            String tableName = entry.getKey();
            List<AutoTable> list = entry.getValue();
            build(tableName, list);
//            });
        }
    }

    public void build(String tableName, List<AutoTable> list) {
        List<AutoTable> fields = data.get(tableName);
        if (fields.isEmpty()) return;
        list.removeAll(fields);
        if (list.isEmpty()) return;
        String fieldName, name;
        AutoTable field;
        for (int i = 0, size = list.size(); i < size; i++) {
            field = list.get(i);
            fieldName = field.getField();
            for (AutoTable autoTable : fields) {
                name = autoTable.getField();
                if (fieldName.equals(name)) {
                    updateSQL(field);
                    list.remove(field);
                    size = list.size();
                }
            }
        }
        if (list.isEmpty()) return;
        for (AutoTable autoTable : list) {
            field = autoTable;
            addField(field);
        }
    }

    public void updateSQL(AutoTable field) {
        StringBuilder sb = new StringBuilder();
        String type, fieldName;
        sb.append("ALTER TABLE `").append(field.getTableName()).append("` CHANGE ")
                .append(CamelMapping.parseString(field.getField())).append(" ")
                .append(fieldName = field.getFieldName()).append(" ")
                .append(type = field.getType());
        if (Type.isString(type) || Type.isNumber(type)) {
            sb.append("(").append(field.getSize()).append(") ");
        } else if (Type.isDecimal(type)) {
            sb.append("(").append(field.getSize()).append(",").append(field.getDecimals()).append(") ");
        } else if (Type.isTime(type) && "TIMESTAMP".equals(type)) {
            if ("create_time".equals(fieldName)) sb.append(" DEFAULT CURRENT_TIMESTAMP ");
            if ("update_time".equals(fieldName))
                sb.append(" DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ");
        } else {
            sb.append(" ");
        }
        if (null != field.getDefaults() && !"".equals(field.getDefaults()))
            sb.append("DEFAULT '").append(field.getDefaults()).append("'");
        if (!field.isNull()) sb.append(" NOT NULL ");
        if (null != field.getFieldRemark() && !"".equals(field.getFieldRemark()))
            sb.append("COMMENT '").append(field.getFieldRemark()).append("'");
        if (!conn.execute(sb.toString())) return;
        executeService.deleteData(field);
        executeService.addData(field);
    }

    public void addField(AutoTable field) {
        StringBuilder sb = new StringBuilder();
        String type, fieldName;
        sb.append("ALTER TABLE `").append(field.getTableName()).append("` ADD ")
                .append(fieldName = field.getFieldName()).append(" ")
                .append(type = field.getType());
        if (Type.isString(type) || Type.isNumber(type)) {
            sb.append("(").append(field.getSize()).append(") ");
        } else if (Type.isDecimal(type)) {
            sb.append("(").append(field.getSize()).append(",").append(field.getDecimals()).append(") ");
        } else if (Type.isTime(type) && "TIMESTAMP".equals(type)) {
            if ("create_time".equals(fieldName)) sb.append(" DEFAULT CURRENT_TIMESTAMP ");
            if ("update_time".equals(fieldName)) {
                sb.append(" DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ");
            } else {
                sb.append(" DEFAULT CURRENT_TIMESTAMP ");
            }
        } else {
            sb.append(" ");
        }
        if (null != field.getDefaults() && !"".equals(field.getDefaults()))
            sb.append("DEFAULT '").append(field.getDefaults()).append("'");
        if (!field.isNull() && !"TIMESTAMP".equals(type)) sb.append(" NOT NULL ");
        if (null != field.getFieldRemark() && !"".equals(field.getFieldRemark()))
            sb.append("COMMENT '").append(field.getFieldRemark()).append("'");
        if (!conn.execute(sb.toString())) return;
        executeService.addData(field);
    }
}
