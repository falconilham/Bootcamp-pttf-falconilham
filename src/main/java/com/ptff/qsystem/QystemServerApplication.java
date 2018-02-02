package com.ptff.qsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableCaching
@SpringBootApplication
public class QystemServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(QystemServerApplication.class, args);
	}
}
