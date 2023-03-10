package com.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @ClassName EnumInteger
 * @Description TODO
 * @Author QiBin
 * @Date 2022/6/23 10:51
 * @Version 1.0
 **/
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(EnumInteger.List.class)
@Documented
@Constraint(validatedBy = EnumNumberValidator.class)
public @interface EnumInteger {

    String message() default "value not in enum values.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * @return date must in this value array
     */
    int[] value();

    /**
     * Defines several {@link EnumInteger} annotations on the same element.
     *
     * @see EnumInteger
     */
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        EnumInteger[] value();
    }
}
