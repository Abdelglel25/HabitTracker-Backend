package com.sa.habittrackerbackend.utils.testcontainers.mariadb;

import org.testcontainers.containers.MariaDBContainer;

public class MariaDBTestContainer extends MariaDBContainer<MariaDBTestContainer> {
    private static final String IMAGE_VERSION = "mariadb:11.2";
    private static MariaDBTestContainer mariaDBContainer;

    private MariaDBTestContainer() {
        super(IMAGE_VERSION);
    }

    public static MariaDBTestContainer getInstance() {
        if (mariaDBContainer == null) {
            mariaDBContainer = new MariaDBTestContainer()
                    .withDatabaseName("habittracker")
                    .withUsername("testContainerUser")
                    .withPassword("testContainerPass");
        }

        return mariaDBContainer;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("spring.datasource.url", mariaDBContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", mariaDBContainer.getUsername());
        System.setProperty("spring.datasource.password", mariaDBContainer.getPassword());
    }
}
