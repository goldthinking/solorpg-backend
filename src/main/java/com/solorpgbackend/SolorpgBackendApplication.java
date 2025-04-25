package com.solorpgbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.solorpgbackend.mapper")
public class SolorpgBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SolorpgBackendApplication.class, args);
	}

}
