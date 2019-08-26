package com.nagisazz.dao;

import com.nagisazz.entity.Number;
import org.apache.ibatis.annotations.Param;

public interface NumberMapper {

    Integer getNumber(@Param("id") Long id);

    void update(Number number);
}
