package com.rezilux.dinngdonngecommerceapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class DinngDonngEcommerceApiApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		setRegisterErrorPageFilter(false);
		return application.sources(DinngDonngEcommerceApiApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(DinngDonngEcommerceApiApplication.class, args);
	}

}
