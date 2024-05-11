package adcsistemas.loja_comprebem;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


@SpringBootApplication
@EnableAsync
@EnableScheduling
@EntityScan(basePackages = "adcsistemas.loja_comprebem.model")
@ComponentScan(basePackages = {"adcsistemas.*"})
@EnableJpaRepositories(basePackages = {"adcsistemas.loja_comprebem.repository"})
@EnableTransactionManagement
@EnableWebMvc
public class LojaComprebemApplication implements AsyncConfigurer, WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(LojaComprebemApplication.class, args);
		
		//System.out.println(new BCryptPasswordEncoder().encode("123")); 
		
		
	}
	
	@Bean
	public ViewResolver viewResolver() {
		
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		
		viewResolver.setPrefix("classpath:templates/");
		viewResolver.setSuffix("html");
		
		return viewResolver;
	}
 	

	@Override
	@Bean
	public Executor getAsyncExecutor() {
		
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(20);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("Assyncrono Thread");
		executor.initialize();
		
		return executor;
	}
	
	
	

	//@Override
	//public void addCorsMappings(CorsRegistry registry) {
		
	//	registry.addMapping("*")
	//	.allowedOrigins("*")
	//	.allowedHeaders("*")
	//	.allowedMethods("*")
	//	.exposedHeaders("*");
		
		//WebMvcConfigurer.super.addCorsMappings(registry);
	//}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
	        CorsConfiguration configuration = new CorsConfiguration();
	        configuration.setAllowCredentials(true);
	        configuration.addAllowedOrigin("http://localhost:4200");// acesso local
	        configuration.addAllowedHeader("*");
	        configuration.addAllowedMethod("*");
	        configuration.addAllowedOriginPattern("*");
	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        source.registerCorsConfiguration("/**", configuration);
	        return source;
	    }
}
