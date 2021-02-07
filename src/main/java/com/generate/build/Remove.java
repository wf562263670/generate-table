package com.generate.build;

import com.generate.config.AutoTable;
import com.generate.mapping.CamelMapping;
import com.generate.service.ExecuteService;
import com.generate.service.ScanService;
import com.generate.utils.DBConnection;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class Remove {

    private final DBConnection conn = new DBConnection();
    private final ExecuteService executeService = new ExecuteService();
    private final Map<String, List<AutoTable>> tableData = ScanService.getTableData();
    private final Map<String, List<AutoTable>> data = ExecuteService.tableData();

    public void remove(ExecutorService executorService) {
        for (Map.Entry<String, List<AutoTable>> entry : data.entrySet()) {
//            executorService.execute(() -> {
            String tableName = entry.getKey();
            List<AutoTable> list = entry.getValue();
            build(tableName, list);
//            });
        }
    }

    public void build(String tableName, List<AutoTable> list) {
        List<AutoTable> fields = tableData.get(tableName);
        if (fields.isEmpty()) return;
        list.removeAll(fields);
        if (list.isEmpty()) return;
        for (AutoTable autoTable : list) {
            removeSQL(autoTable);
        }
    }

    public void removeSQL(AutoTable autoTable) {
        String sql = "ALTER TABLE `" + autoTable.getTableName() + "` DROP COLUMN `" + CamelMapping.parseCamel(autoTable.getField()) + "`";
        if (!conn.execute(sql)) return;
        executeService.deleteData(autoTable);
    }

}
