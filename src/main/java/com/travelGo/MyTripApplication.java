package com.travelGo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MyTripApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyTripApplication.class, args);
	}

}
