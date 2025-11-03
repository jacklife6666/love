package com.xs.springboot;

import com.xs.springboot.dao.IUserDao;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import com.xs.springboot.service.UserService;

@SpringBootApplication
public class SpringbootApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext ioc = SpringApplication.run(SpringbootApplication.class, args);
		UserService userService = ioc.getBean(UserService.class);

		userService.getUser();
	}

}
