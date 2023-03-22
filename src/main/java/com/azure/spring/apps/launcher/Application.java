package com.azure.spring.apps.launcher;

import de.codecentric.boot.admin.client.config.SpringBootAdminClientAutoConfiguration;
import de.codecentric.boot.admin.client.config.SpringBootAdminClientCloudFoundryAutoConfiguration;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = { SpringBootAdminClientAutoConfiguration.class,
    SpringBootAdminClientCloudFoundryAutoConfiguration.class })
@EnableAdminServer
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
