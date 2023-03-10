package com.mybatisPlus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;



/**
 * @ClassName MyMetaObjectHandler
 * @Description TODO
 * @Author QiBin
 * @Date 2022/8/29 15:14
 * @Version 1.0
 **/


import java.util.Date;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName( "createTime",  new Date(),metaObject);
        this.setFieldValByName( "updateTime", new Date(),metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName( "updateTime", new Date(),metaObject);
    }
}
