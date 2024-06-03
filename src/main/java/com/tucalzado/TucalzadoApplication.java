package com.tucalzado;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class TucalzadoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TucalzadoApplication.class, args);
	}

}
