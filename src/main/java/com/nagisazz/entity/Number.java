package com.nagisazz.entity;

import lombok.Builder;
import lombok.Data;

/**
 * @auther zhushengzhe
 * @date 2019/8/23 14:17
 */
@Data
@Builder
public class Number {

    private Long id;

    private Integer number;
}
