package adcsistemas.loja_comprebem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan(basePackages = "adcsistemas.loja_comprebem.model")
@ComponentScan(basePackages = {"adcsistemas.*"})
@EnableJpaRepositories(basePackages = {"adcsistemas.loja_comprebem.repository"})
@EnableTransactionManagement
public class LojaComprebemApplication {

	public static void main(String[] args) {
		SpringApplication.run(LojaComprebemApplication.class, args);
	}

}
