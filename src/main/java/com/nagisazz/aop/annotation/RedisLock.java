package com.nagisazz.aop.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisLock {

    /**
     * redis key的值，默认全包名+类名+方法名
     *
     * @param :
     * @return : java.lang.String
     */
    String key() default "";

    /**
     * redis value的值，默认UUID
     *
     * @param :
     * @return : java.lang.String
     */
    String value() default "";

    /**
     * 过期时间，默认30s
     *
     * @param :
     * @return : int
     */
    int expire() default 30;
}
