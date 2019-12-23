package com.demo.interview.springboot.norepeat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解NoRepeatSubmit.java
 */
//@Target(ElementType.METHOD) // 作用到方法上
//@Retention(RetentionPolicy.RUNTIME) // 运行时有效
public interface NoRepeatSubmit {
}
