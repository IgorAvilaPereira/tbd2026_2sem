package br.edu.ifrs.minicurso.springsolidapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SpringSolidApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSolidApiApplication.class, args);
	}

}
