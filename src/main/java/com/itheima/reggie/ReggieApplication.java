package com.itheima.reggie;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.DigestUtils;

import javax.servlet.annotation.WebFilter;

@Slf4j
@SpringBootApplication
@ServletComponentScan
//@MapperScan("com.itheima.mapper")
public class ReggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class,args);
        log.info("项目启动成功...");
//        String str = "123456";
//        String s = DigestUtils.md5DigestAsHex(str.getBytes());
//        System.out.println(s);
    }
}