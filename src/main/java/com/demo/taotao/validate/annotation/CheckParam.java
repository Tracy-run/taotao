package com.demo.taotao.validate.annotation;


import com.demo.taotao.validate.enumtype.Check;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface CheckParam {


    // 字段校验规则，格式：字段名+校验规则+冒号+错误信息，例如：id<10:ID必须少于10
    Check value() default Check.NotNull;

    // 多个值逗号隔开
    String express() default "";

    // 参数名称用.表示层级，最多支持2级如： entity.userName
    String argName();

    // 自定义错误提示信息
    String msg() default "";

}
