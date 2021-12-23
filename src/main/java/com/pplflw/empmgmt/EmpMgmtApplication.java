package com.pplflw.empmgmt;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author mkumar
 * @version 1.0
 * @since 1.0
 */
@SpringBootApplication
public class EmpMgmtApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmpMgmtApplication.class, args);
    }

    @Bean
    public OpenAPI customOpenAPI(@Value("${springdoc.version}") String appVersion, @Value("${springdoc.description}") String appDescription) {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Employee On-boarding API")
                        .version(appVersion)
                        .description(appDescription)
                        .contact(new Contact().email("manish2aug@gmail.com").name("Manish Kumar")));
    }
}
