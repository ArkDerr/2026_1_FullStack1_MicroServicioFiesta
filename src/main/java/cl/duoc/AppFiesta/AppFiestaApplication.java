package cl.duoc.AppFiesta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient 
public class AppFiestaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppFiestaApplication.class, args);
	}

}
