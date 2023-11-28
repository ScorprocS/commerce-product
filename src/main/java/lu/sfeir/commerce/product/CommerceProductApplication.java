package lu.sfeir.commerce.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EnableFeignClients
public class CommerceProductApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommerceProductApplication.class, args);
	}

}
