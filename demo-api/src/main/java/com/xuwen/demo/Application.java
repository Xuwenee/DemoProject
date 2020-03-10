package com.xuwen.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 启动入口
 * ServletComponentScan支持过滤器注解注入
 * @author XuWen
 * @Date 2018/4/16 20:16
 */
//@EnableDiscoveryClient
@ServletComponentScan
@SpringBootApplication
@EnableTransactionManagement
@MapperScan(basePackages = "com.xuwen.demo.dao.mapper")
@EnableAsync
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
