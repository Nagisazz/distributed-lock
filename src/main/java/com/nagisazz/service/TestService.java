package com.nagisazz.service;

import com.nagisazz.aop.annotation.RedisLock;
import com.nagisazz.dao.NumberMapper;
import com.nagisazz.entity.Number;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @auther zhushengzhe
 * @date 2019/8/23 14:11
 */
@Service
public class TestService {

    @Autowired
    private NumberMapper numberMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @RedisLock
    public void reduce() {
        Integer num = numberMapper.getNumber(Long.valueOf(1));
        if (num > 0){
            numberMapper.update(Number.builder().id(Long.valueOf(1)).build());
        }
    }
}
