package com.generate.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

    //    表名
    String name() default "";

    //    注释
    String remark() default "";
}
