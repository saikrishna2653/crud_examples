package eu.dreamix.crud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class CrudApp {
	public static void main(String[] args) {
		SpringApplication.run(CrudApp.class, args);
	}
}
