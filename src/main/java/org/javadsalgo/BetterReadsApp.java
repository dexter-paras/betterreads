package org.javadsalgo;

import org.javadsalgo.connection.DataStaxAstraProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.nio.file.Path;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
/*@EnableConfigurationProperties(DataStaxAstraProperties.class)*/
public class BetterReadsApp {

    public static void main(String[] args) {
        System.out.println("Main method called");
        SpringApplication.run(BetterReadsApp.class, args);
    }

    /**
     * This is necessary to have the Spring Boot app use the Astra secure bundle
     * to connect to the database
     */
    @Bean
    public CqlSessionBuilderCustomizer sessionBuilderCustomizer(/*DataStaxAstraProperties astraProperties*/) {
        Path bundle = new File("secure-connect.zip").toPath();
        System.out.println("Path looks like "+ bundle.getFileName().toString());
        System.out.println("Path---"+ bundle.toAbsolutePath());
        return builder -> builder.withCloudSecureConnectBundle(bundle);
    }
}
