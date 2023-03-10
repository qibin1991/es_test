package com.mybatisPlus.enumType;

/**
 * @ClassName QueryWapper
 * @Description TODO
 * @Author QiBin
 * @Date 2022/8/30 18:38
 * @Version 1.0
 **/
import org.springframework.stereotype.Indexed;

import java.lang.annotation.*;


@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface QueryWapper {

    String field() default "";

    QueryWapperEnum queryWapperEnum() default QueryWapperEnum.EQ ;
}
