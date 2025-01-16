package com.yupi.yupicturebackend.annotation;

import net.bytebuddy.implementation.bind.annotation.RuntimeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) //生效范围  方法生效
@Retention(RetentionPolicy.RUNTIME) //生效时间  运行时生效
public @interface AuthCheck {
    /**
     * 必须有某个角色
     */
    String mustRole() default "";
}
