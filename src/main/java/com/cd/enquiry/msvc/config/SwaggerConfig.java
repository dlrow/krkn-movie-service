
package com.cd.enquiry.msvc.config;

import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2).select().
				apis(RequestHandlerSelectors.basePackage("com.cd.enquiry"))
				.paths(regex("/.*")).build().apiInfo(metaData());
	}

	private ApiInfo metaData() {
		ApiInfo apiInfo = new ApiInfo("", "", "", "", "", "", "");
		return apiInfo;
	}
	
	/*@Bean
    public JavaMailSenderImpl mailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setProtocol("SMTP");
        javaMailSender.setHost("127.0.0.1");
        javaMailSender.setPort(587);

        return javaMailSender;
    }*/
}
