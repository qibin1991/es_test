package com.valid;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

/**
 * @ClassName Artisan
 * @Description TODO
 * @Author QiBin
 * @Date 2022/6/23 10:48
 * @Version 1.0
 **/
@Data
public class Artisan {

    /**
     @Null	用在基本类型上；限制只能为null
     @NotNull	用在基本类型上；不能为null，但可以为empty
     @NotEmpty	用在集合类上面；不能为null，而且长度必须大于0
     @NotBlank	用在String上面；只能作用在String上，不能为null，而且调用trim()后，长度必须大于0
     @AssertFalse	限制必须为false
     @AssertTrue	限制必须为true
     @Digits	验证 Number 和 String 的构成是否合法
     @DecimalMax(value)	限制必须为一个不大于指定值的数字,小数存在精度
     @DecimalMin(value)	限制必须为一个不小于指定值的数字,小数存在精度
     @Digits(integer,fraction)	限制必须为一个小数，且整数部分的位数不能超过integer，小数部分的位数不能超过fraction
     @Size(max,min)	限制字符长度必须在min到max之间
     @Max(value)	限制必须为一个不大于指定值的数字
     @Min(value)	限制必须为一个不小于指定值的数字
     @Past	限制必须是一个过去的日期
     @Future	限制必须是一个将来的日期
     @Pattern(value)	限制必须符合指定的正则表达式
     @Past	验证注解的元素值（日期类型）比当前时间早
     @Email	验证注解的元素值是Email，也可以通过正则表达式和flag指定自定义的email
     */


    private String id;

    @NotEmpty(message = "Code不能为空")
    private String code;

    @NotBlank(message = "名字为必填项")
    private String name;


    @Length(min = 8, max = 12, message = "password长度必须位于8到12之间")
    @NotNull(groups = CustomValidateGroup.Crud.Create.class,message = "新增接口密码不能为空")
    @Null(groups = CustomValidateGroup.Crud.Update.class)
    private String password;


    @Email(message = "请填写正确的邮箱地址")
    private String email;

    @EnumString(value = {"F", "M"}, message = "性别只允许为F或M",groups = {})
    public String sex;



}
