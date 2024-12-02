package com.swyp.mema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MemaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MemaApplication.class, args);
	}
}
