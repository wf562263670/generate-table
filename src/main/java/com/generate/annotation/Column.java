package com.generate.annotation;


import com.generate.config.Type;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    //    字段名
    String name() default "";

    //    字段类型
    Type type() default Type.VARCHAR;

    //    字段大小
    int size() default 100;

    //    小数点大小
    int decimal() default 0;

    //    默认值
    String defaults() default "";

    //    是否为主键?true:false
    boolean isKey() default false;

    //    是否为空?true:false
    boolean isNull() default true;

    //    是否自增?true:false
    boolean isAutoIncrement() default false;

    //    备注
    String remark() default "";

}
