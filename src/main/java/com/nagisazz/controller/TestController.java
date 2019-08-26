package com.nagisazz.controller;

import com.nagisazz.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther zhushengzhe
 * @date 2019/8/23 14:11
 */
@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/test/reduce")
    public void reduce() {
        testService.reduce();

//        for (int i = 0; i < 110; i++) {
//            new Thread(){
//                @Override
//                public void run() {
//                    testService.reduce();
//                }
//            }.start();
//        }
    }
}
