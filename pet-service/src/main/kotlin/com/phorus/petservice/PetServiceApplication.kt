package com.phorus.petservice

import io.r2dbc.spi.ConnectionFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ClassPathResource
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator

@SpringBootApplication
class PetServiceApplication {

    @Bean
    fun initializer(connectionFactory: ConnectionFactory?): ConnectionFactoryInitializer {
        val initializer = ConnectionFactoryInitializer()
        initializer.setConnectionFactory(connectionFactory!!)
        // This will create our database tables and schema
        initializer.setDatabasePopulator(ResourceDatabasePopulator(ClassPathResource("sql/schema.sql")))
        // This will drop our tables after we are done so we can have a fresh start next run
        initializer.setDatabaseCleaner(ResourceDatabasePopulator(ClassPathResource("sql/cleanup.sql")))
        return initializer
    }
}

fun main(args: Array<String>) {
    runApplication<PetServiceApplication>(*args)
}
