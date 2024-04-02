package com.zythologue.minimal_api.Configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenAPIConfig {

    @Bean
  public OpenAPI myOpenAPI() {

    Contact contact = new Contact();
    contact.setEmail("theo.duflos.yokoko@gmail.com");
    contact.setName("Théo 'Yokoko' Duflos");

    License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

    Info info = new Info()
        .title("Zythologue API")
        .version("1.0")
        .contact(contact)
        .description("Cette api est destinée aux utilisateurs de l'application Zythologue")
        .license(mitLicense);

      return new OpenAPI().info(info);
  }
}
