package com.mybatisPlus;

/**
 * @ClassName QueryWrapperUtil
 * @Description TODO
 * @Author QiBin
 * @Date 2022/8/30 18:37
 * @Version 1.0
 **/
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.yulichang.query.MPJQueryWrapper;
import com.mybatisPlus.enumType.QueryWapper;
import com.mybatisPlus.enumType.QueryWapperEnum;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;

import static org.apache.commons.lang3.reflect.MethodUtils.invokeMethod;

/**
 * @Description 拼接查询条件工具类
 */
public class QueryWrapperUtil {

    /**
     * 拼接查询条件
     *
     * @param queryWrapper 条件对象
     * @param obj          数据实体
     * @return void 返回参数说明
     * @exception/throws
     */
    public static void convertQuery(QueryWrapper queryWrapper, Object obj) {
        Class clazz = obj.getClass();
        try {
            // 反射遍历属性
            for (Field field : clazz.getDeclaredFields()) {
                // 获取属性名
                String fieldname = field.getName();
                // 抑制Java对修饰符的检查
                field.setAccessible(true);
                // 获取属性值
                Object fieldValue =  field.get(obj);
//            String fieldValue = getFieldValue(obj ,field.getName()).toString();
                // 查询注解
                QueryWapper queryWapperAnnotation = AnnotationUtils.getAnnotation(field, QueryWapper.class);
                if(ObjectUtils.isEmpty(queryWapperAnnotation)){
                    continue;
                }
                String fieldName = queryWapperAnnotation.field();
                // 获取枚举
                QueryWapperEnum queryWapperEnum = queryWapperAnnotation.queryWapperEnum();
                // 拼接查询条件
                switch (queryWapperEnum) {
                    case EQ:
                        queryWrapper.eq(!ObjectUtils.isEmpty(fieldValue), fieldName, fieldValue);
                        break;
                    case LIKE:
                        queryWrapper.like(!ObjectUtils.isEmpty(fieldValue), fieldName, fieldValue);
                        break;
                    default:
                        break;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取属性名
     *
     * @exception/throws
     */
    private static String getFieldValue(Object owner, String fieldName) {
        try {
            return invokeMethod(owner, fieldName, null).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
