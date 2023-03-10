package com.report;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * copyProperties
 *
 * @author yzw
 * @date 2019/08/28 16:03
 */
public class CopyUtils {

    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(CopyUtils.class);

    /**
     * @param sourceObj
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> T copyObject(Object sourceObj, Class<T> targetClass) {
        if (sourceObj == null) {
            return null;
        }

        T target = null;
        try {
            target = targetClass.newInstance();
            BeanUtils.copyProperties(sourceObj, target);
        } catch (Exception e) {
            logger.error("convert error. msg={}", e.getMessage());
        }

        return target;
    }

    /**
     * @param sourceList
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> List<T> copyList(List<?> sourceList, Class<T> targetClass) {
        if (sourceList == null) {
            return null;
        }

        List targetList = new ArrayList(sourceList.size());
        try {
            for (Object obj : sourceList) {
                T target = targetClass.newInstance();
                BeanUtils.copyProperties(obj, target);
                targetList.add(target);
            }
        } catch (Exception e) {
            logger.error("convert error. msg={}", e.getMessage());
        }

        return targetList;
    }

    public static <T> List<T> copyEnumList(List<?> sourceList, Class<T> targetClass) {
        if (sourceList == null) {
            return null;
        }

        List targetList = new ArrayList(sourceList.size());
        try {
            for (Object obj : sourceList) {
                T target = targetClass.newInstance();
                BeanUtils.copyProperties(obj, target);
                targetList.add(target);
            }
        } catch (Exception e) {
            logger.error("convert error. msg={}", e.getMessage());
        }

        return targetList;
    }

    /**
     * targetList地址不变
     *
     * @param sourceList
     * @param targetList
     * @param targetClass
     * @param <T>
     */
    public static <T> void copyToList(List<?> sourceList, List<T> targetList, Class<T> targetClass) {
        if (sourceList == null || targetList == null) {
            return;
        }

        targetList.clear();

        try {
            for (Object obj : sourceList) {
                T target = targetClass.newInstance();
                BeanUtils.copyProperties(obj, target);
                targetList.add(target);
            }
        } catch (Exception e) {
            logger.error("convert error. msg={}", e.getMessage());
        }
    }

    /**
     * 使用json进行复制
     *
     * @param sourceList
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> List<T> copyListByJson(List<?> sourceList, Class<T> targetClass) {
        if (sourceList == null) {
            return null;
        }

        List targetList = new ArrayList(sourceList.size());
        try {
//            targetList = JSON.parseArray(JSON.toJSONString(sourceList), targetClass);
            targetList = JsonHelper.json2List(JsonHelper.toJson(sourceList), targetClass);
        } catch (Exception e) {
            logger.error("convert error. msg={}", e.getMessage());
        }

        return targetList;
    }

    /**
     * 使用json进行复制
     *
     * @param sourceObj
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> T copyObjectByJson(Object sourceObj, Class<T> targetClass) {
        if (sourceObj == null) {
            return null;
        }

        T target = null;
        try {
//            target = JSON.parseObject(JSON.toJSONString(sourceObj), targetClass);
            target = JsonHelper.fromJson(JsonHelper.toJson(sourceObj), targetClass);
        } catch (Exception e) {
            logger.error("convert error. msg={}", e.getMessage());
        }

        return target;
    }

}
