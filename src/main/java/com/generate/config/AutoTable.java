package com.generate.config;

import com.generate.annotation.Column;
import com.generate.annotation.Table;
import lombok.Data;

@Data
@Table(remark = "结构表")
public class AutoTable {

    @Column(type = Type.VARCHAR, size = 40, isKey = true, isNull = false, remark = "表名称")
    private String tableName;

    @Column(type = Type.VARCHAR, size = 40, remark = "表备注")
    private String tableRemark;

    @Column(type = Type.VARCHAR, size = 40, isNull = false, remark = "对象字段")
    private String field;

    @Column(type = Type.VARCHAR, size = 40, isNull = false, remark = "表字段")
    private String fieldName;

    @Column(type = Type.VARCHAR, size = 10, isNull = false, remark = "类型")
    private String type;

    @Column(type = Type.INT, size = 5, isNull = false, remark = "长度")
    private int size;

    @Column(type = Type.VARCHAR, size = 5, isNull = false, remark = "小数长度")
    private int decimals;

    @Column(type = Type.VARCHAR, size = 40, remark = "默认值")
    private String defaults;

    @Column(type = Type.VARCHAR, size = 5, isNull = false, remark = "是否为主键")
    private boolean isKey;

    @Column(type = Type.VARCHAR, size = 5, isNull = false, remark = "是否为空")
    private boolean isNull;

    @Column(type = Type.VARCHAR, size = 5, isNull = false, remark = "是否自增")
    private boolean isAutoIncrement;

    @Column(type = Type.VARCHAR, size = 40, remark = "字段备注")
    private String fieldRemark;

}
