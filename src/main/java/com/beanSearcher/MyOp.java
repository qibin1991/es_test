package com.beanSearcher;

/**
 * @ClassName MyOp
 * @Description TODO
 * @Author QiBin
 * @Date 2022/5/9 19:00
 * @Version 1.0
 **/

import com.ejlchina.searcher.FieldOp;
import com.ejlchina.searcher.SqlWrapper;

import java.util.List;

/**
 * 自定义运算符
 */
public class MyOp implements FieldOp {

    @Override
    public String name() {
        return "my";
    }

    @Override
    public boolean isNamed(String name) {
        return "my".equals(name);
    }

    @Override
    public boolean lonely() {
        return true;
    }

    @Override
    public List<Object> operate(StringBuilder sqlBuilder, OpPara opPara) {
        SqlWrapper<Object> fieldSql = opPara.getFieldSql();
        sqlBuilder.append(fieldSql.getSql()).append(" = 25");
        return fieldSql.getParas();
    }
}
