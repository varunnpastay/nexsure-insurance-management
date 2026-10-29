package com.Dcl.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig {

	@Bean
	public ModelMapper mapper() {
		ModelMapper mapper=new ModelMapper();
		//STRICT = copy only fields with the exact same name (avoids wrong guesses on nested objects)
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return mapper;
	}
	
	//allows the React app (running on port 5173) to call this API
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("http://localhost:5173")
						.allowedMethods("GET", "POST", "PUT", "DELETE");
			}
		};
	}
	
}
