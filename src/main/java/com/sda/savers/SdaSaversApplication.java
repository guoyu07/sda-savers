package com.sda.savers;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.sda.savers.dao.mapper")
public class SdaSaversApplication {

	public static void main(String[] args) {
		SpringApplication.run(SdaSaversApplication.class, args);
	}
}
