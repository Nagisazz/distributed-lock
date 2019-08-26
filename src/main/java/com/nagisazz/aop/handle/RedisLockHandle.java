package com.nagisazz.aop.handle;

import com.nagisazz.aop.annotation.RedisLock;
import com.nagisazz.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collections;

/**
 * @auther zhushengzhe
 * @date 2019/8/23 17:08
 */
@Aspect
@Component
@Slf4j
public class RedisLockHandle {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String OK_STRING = "OK";
    private static final Long DELETE_NUMBER = 0L;
    private static final String SPLIT = ".";
    private static final Integer SLEEP_TIME = 10;

    @Around("execution(public * * (..) ) && @annotation(com.nagisazz.aop.annotation.RedisLock)")
    public Object handle(ProceedingJoinPoint point) {

        Object[] args = point.getArgs();
        Method me = ((MethodSignature) point.getSignature()).getMethod();
        String className = point.getTarget().getClass().getName();
        String methodName = me.getName();
        RedisLock redisLock = me.getAnnotation(RedisLock.class);
        String key = redisLock.key();
        String value = redisLock.value();
        if ("".equals(redisLock.key())) {
            key = className + SPLIT + methodName;
        }
        if ("".equals(redisLock.value())) {
            value = UUIDUtil.getUUIDWithoutConnector();
        }

        Object result = null;
        try {
            //若是获取不到锁，则直接返回
            while (true) {
                if (setNxEx(key, value, redisLock.expire())) {
                    //获取到锁，执行方法
                    result = point.proceed(args);
                    break;
                } else {
                    Thread.sleep(SLEEP_TIME);
                }
            }
        } catch (Throwable throwable) {
            log.error(throwable.getMessage());
        } finally {
            //释放锁
            delete(key, value);
        }

        return result;
    }

    /**
     * 设置锁
     *
     * @param key
     * @param value
     * @param expire
     * @return : java.lang.Boolean
     */
    private Boolean setNxEx(String key, String value, int expire) {
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            String result = (String) connection.execute("set", key.getBytes(), value.getBytes(), "nx".getBytes(), "ex".getBytes(), String.valueOf(expire).getBytes());
            if (result == null) {
                return false;
            }
            return OK_STRING.equals(result);
        });
    }

    /**
     * 释放锁
     *
     * @param key
     * @param value
     * @return : boolean
     */
    private boolean delete(String key, String value) {
        // lua script
        String script = "if redis.call('EXISTS', KEYS[1]) == 1 then if redis.call('GET', KEYS[1]) == ARGV[1] " +
                "then return redis.call('DEL', KEYS[1]) else return 0 end else return 0 end";

        //执行 lua 脚本
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<Long>();
        redisScript.setScriptText(script);
        redisScript.setResultType(Long.class);
        Object result = redisTemplate.execute(redisScript, Collections.singletonList(key), new String[]{value});
        //返回是否成功删除
        return !DELETE_NUMBER.equals(result);
    }

}
