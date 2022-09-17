package com._247ffa.status.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static int MILLISECONDS_IN_HOUR = 3600000;
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}


}
