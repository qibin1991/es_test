package com.mybatisPlus.enumType;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @description： 性别枚举
 * @author：weirx
 * @date：2022/1/17 16:26
 * @version：3.0
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SexEnum implements IEnum<Integer> {
    MAN(1, "男"),
            WOMAN(2, "女");
    @EnumValue
    private Integer code;
    private String name;

    SexEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public Integer getValue() {
        return code;
    }

    public String getName() {
        return name;
    }

    }
