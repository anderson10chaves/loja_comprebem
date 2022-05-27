package adcsistemas.loja_comprebem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "adcsistemas.loja_comprebem.model")
public class LojaComprebemApplication {

	public static void main(String[] args) {
		SpringApplication.run(LojaComprebemApplication.class, args);
	}

}
