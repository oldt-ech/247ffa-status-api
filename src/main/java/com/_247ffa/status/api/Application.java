package com._247ffa.status.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static int SECONDS_IN_HOUR = 3600;
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}


}
