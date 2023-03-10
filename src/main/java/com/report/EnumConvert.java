package com.report;

import java.util.HashMap;
import java.util.Map;

public interface EnumConvert{
    //将映射关系缓存到Map中，便于快速查找
    Map<String,Object> map = new HashMap<>();

    //key映射道value
    default Object mapping(Object key){
        this.initMap();
        return map.get(String.valueOf(key));
    }

    default void initMap(){
        if(!map.isEmpty()){
            return;
        }
        for (EnumConvert item : getValues()) {
            map.put(String.valueOf(item.getKey()), item.getValue());
        }
    }

    Object getKey();
    // 返回的实际类型必须和目标属性的类型一致
    Object getValue();
    EnumConvert[] getValues();

}
