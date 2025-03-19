package com.ylab.homework_1;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Properties;

public class TestContainerConfig {
    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER;
    private static final Properties properties;

    static {
        POSTGRES_CONTAINER = new PostgreSQLContainer<>(DockerImageName.parse("postgres:15"))
                .withDatabaseName("finance_db")
                .withUsername("finance_user")
                .withPassword("finance_password");
        POSTGRES_CONTAINER.start();
        properties = new Properties();
        properties.setProperty("db.url", POSTGRES_CONTAINER.getJdbcUrl());
        properties.setProperty("db.user", POSTGRES_CONTAINER.getUsername());
        properties.setProperty("db.password", POSTGRES_CONTAINER.getPassword());
        properties.setProperty("liquibase.change-log", "db/migration/changelog.xml");
        properties.setProperty("liquibase.default-schema", "finance");
    }

    public static Properties getProperties() {
        return properties;
    }
}