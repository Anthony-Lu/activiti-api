package com.fairy.activiti;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * 启动类
 * @author luxuebing
 * @date 2018/02/01下午5:16:50
 */
@SpringBootApplication
@MapperScan("com.fairy.activiti.dao")
public class ActivitiTestLxbApplication extends SpringBootServletInitializer{

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ActivitiTestLxbApplication.class);
    }
	
	public static void main(String[] args) {
		SpringApplication.run(ActivitiTestLxbApplication.class, args);
	}
}
