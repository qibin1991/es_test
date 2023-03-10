package com.i18n;


import com.alibaba.fastjson.JSONObject;
import com.arbonstop.http.AjaxResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * @ClassName LanguageAspect
 * @Description TODO
 * @Author QiBin
 * @Date 2022/9/14 10:27
 * @Version 1.0
 **/
@Aspect
@Component
@ConditionalOnProperty(prefix = "lang", name = "open", havingValue = "true")
public class LanguageAspect {

    //@ConditionalOnProperty是读取yml中配置的  lang.open的值，为true则AOP生效，否则不生效。



    private static final Log LOG = LogFactory.getLog(LanguageAspect.class);

    @Autowired
    I18nUtils i18nUtils;

    @Pointcut("execution(public * com.*.*Controller.*(..))")
    public void annotationLangCut() {
    }

    /**
     * 拦截controller层返回的结果，修改msg字段
     *
     * @param point
     * @param resultObject
     */
    @AfterReturning(pointcut = "annotationLangCut()", returning = "resultObject")
    public void around(JoinPoint point, JSONObject resultObject) {
        try {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            //从获取RequestAttributes中获取HttpServletRequest的信息
            HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
            String langFlag = request.getHeader("dimLang");
            if (null != langFlag) {
                String msg = resultObject.getString("result_msg");
                String code = resultObject.getString("result_code");
                if (code!=null&&!"".equals(code)) {
                    code = code.trim();
                    Locale locale = null;
                    if ("zh-CN".equals(langFlag)) {
                        locale = Locale.CHINA;
                    } else if ("en-US".equals(langFlag)) {
                        locale = Locale.US;
                    }

                    if(locale!=null){
                        msg = i18nUtils.getKey(code, locale);
                    }else {
                        msg = i18nUtils.getKey(code);
                    }
                }
                resultObject.put("result_msg",msg);
            }
        } catch (Exception e) {
            LOG.error(e);
        }
    }

}
