package com.microservices.mvc.cursos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MvcCursosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MvcCursosApplication.class, args);
	}

}
