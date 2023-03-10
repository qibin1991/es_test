package com.report;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DiType implements EnumConvert {
    //0排放因子,1,供应商排放因子
    EDITING(0, "排放因子库"),

    FINISH(1, "供应商排放因子");
    private final Integer code;
    private final String desc;

    @Override
    public Object getKey() {
        return code;
    }

    @Override
    public Object getValue() {
        //注意：必须和待转换的目标属性类型相同，否则不会生效
        return desc;
    }

    @Override
    public EnumConvert[] getValues() {
        return DiType.values();
    }

}
