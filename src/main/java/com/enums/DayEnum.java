package com.enums;


public enum DayEnum {
    //指向内部枚举的同一个属性即可执行相同重复功能
    Monday("星期一", Type.ENGLISH),
    Tuesday("星期二", Type.ENGLISH),
    Wednesday("星期三", Type.ENGLISH),

    Thursday("星期四", Type.CHINESE);
    private final Type type;
    private final String day;
    DayEnum(String day, Type type) {
        this.day = day;
        this.type = type;
    }
    String toDo() {
        return type.toDo();
    }

    public Type getType() {
        return type;
    }

    public String getDay() {
        return day;
    }



    public static Type getValue(String code) {
        for (DayEnum value : DayEnum.values()) {
            if (value.getDay().equals(code)) {
                return value.getType();
            }
        }
        return null;
    }


    /**
     * 内部策略枚举
     */
    private enum Type {
        ENGLISH {
            @Override
            public String toDo() {

                return "今天上英语课";
            }
        },
        CHINESE {
            @Override
            public String toDo() {

                return "今天上语文课";
            }
        };
        public abstract String toDo();
    }

}


