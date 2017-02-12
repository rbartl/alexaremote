package com.edvbartl.alexafb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.autoconfigure.web.*;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Import({
		DispatcherServletAutoConfiguration.class,
		EmbeddedServletContainerAutoConfiguration.class,
		ErrorMvcAutoConfiguration.class,
		HttpEncodingAutoConfiguration.class,
		HttpMessageConvertersAutoConfiguration.class,
		JacksonAutoConfiguration.class,
		JmxAutoConfiguration.class,
		MultipartAutoConfiguration.class,
		ServerPropertiesAutoConfiguration.class,
		PropertyPlaceholderAutoConfiguration.class,
		ThymeleafAutoConfiguration.class,
		WebMvcAutoConfiguration.class,
		WebSocketAutoConfiguration.class
})
@Configuration
@ComponentScan
public class AlexaFbApplication {


	public static void main(String[] args) {

		SpringApplication.run(AlexaFbApplication.class, args);
	}
}
