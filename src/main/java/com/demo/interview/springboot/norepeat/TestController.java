package com.demo.interview.springboot.norepeat;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/test")
//    @NoRepeatSubmit   //自定义注解NG
    private String test() {
        return ("程序逻辑返回");
    }

}
