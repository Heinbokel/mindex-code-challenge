package com.mindex.challenge.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        final Contact apiContact = new Contact();
        apiContact.setName("Our Team");
        apiContact.setUrl("https://url.to.our.team.docs");
        apiContact.setEmail("dl.to.our.team");

        return new OpenAPI()
                .info(new Info()
                        .title("Employee Management API")
                        .contact(apiContact)
                        .version("1.0")
                        .description("API for managing employees, direct reports, and compensations."));
    }
}
