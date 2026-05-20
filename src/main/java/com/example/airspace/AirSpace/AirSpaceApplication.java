package com.example.airspace.AirSpace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;


@SpringBootApplication
@EnableMongoAuditing
public class AirSpaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirSpaceApplication.class, args);
	}

}
