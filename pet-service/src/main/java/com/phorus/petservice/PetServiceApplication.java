package com.phorus.petservice;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

@SpringBootApplication
public class PetServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetServiceApplication.class, args);
    }

    @Bean
    ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {

        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        // This will create our database tables and schema
        initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("sql/schema.sql")));
        // This will drop our tables after we are done so we can have a fresh start next run
        initializer.setDatabaseCleaner(new ResourceDatabasePopulator(new ClassPathResource("sql/cleanup.sql")));
        return initializer;
    }
}
