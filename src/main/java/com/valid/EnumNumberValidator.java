package com.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName EnumNumberValidator
 * @Description TODO
 * @Author QiBin
 * @Date 2022/6/23 10:50
 * @Version 1.0
 **/
public class EnumNumberValidator implements ConstraintValidator<EnumInteger, Number> {
    private List<Integer> enumStringList;

    @Override
    public void initialize(EnumInteger constraintAnnotation) {
        enumStringList = new ArrayList<>();
        int[] values = constraintAnnotation.value();
        for (int value : values) {
            enumStringList.add(value);
        }
    }

    @Override
    public boolean isValid(Number value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return enumStringList.contains(value.intValue());
    }
}
