package com.valid;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName EnumStringValidator
 * @Description TODO
 * @Author QiBin
 * @Date 2022/6/23 10:50
 * @Version 1.0
 **/
@Slf4j
public class EnumStringValidator implements ConstraintValidator<EnumString, String> {
    private List<String> enumStringList;

    @Override
    public void initialize(EnumString constraintAnnotation) {
        log.info("EnumStringValidator initialize.....");
        enumStringList = Arrays.asList(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return enumStringList.contains(value);
    }
}
