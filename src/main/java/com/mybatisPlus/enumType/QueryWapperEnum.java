package com.mybatisPlus.enumType;

/**
 * @ClassName QueryWapperEnum
 * @Description TODO
 * @Author QiBin
 * @Date 2022/8/30 18:38
 * @Version 1.0
 **/

import org.springframework.stereotype.Indexed;

import java.lang.annotation.*;


/**
 * @Description 查询枚举类
 * @Date 2020-07-16 16:10
 */
public enum QueryWapperEnum {

    LIKE(1),EQ(2);

    private final int value;

    QueryWapperEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}