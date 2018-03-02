package com.fairy.activiti;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * 启动类
 * 
 * @author luxuebing
 * @date 2018/02/01下午5:16:50
 */
@Configuration
@SpringBootApplication
@MapperScan("com.fairy.activiti.dao")
@EnableAutoConfiguration(exclude = { MultipartAutoConfiguration.class })
public class ActivitiTestLxbApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ActivitiTestLxbApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(ActivitiTestLxbApplication.class, args);
		/*SpringApplication springApplication = new SpringApplication(ActivitiTestLxbApplication.class);
		springApplication.setBannerMode(Banner.Mode.OFF);
		springApplication.run(args);*/
		
	}

	// 显示声明CommonsMultipartResolver为mutipartResolver
	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setDefaultEncoding("UTF-8");
		resolver.setResolveLazily(true);// resolveLazily属性启用是为了推迟文件解析，以在在UploadAction中捕获文件大小异常
		resolver.setMaxInMemorySize(40960);
		resolver.setMaxUploadSize(50 * 1024 * 1024);// 上传文件大小 50M 50*1024*1024
		return resolver;
	}

}
