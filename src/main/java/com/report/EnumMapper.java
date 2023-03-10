package com.report;


import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumMapper {
    //指定枚举Class
    Class<? extends Enum<?>> value();
    //指定源对象中对应的属性别名（属性名相同时，无需指定alias）
    String alias() default "";
}
