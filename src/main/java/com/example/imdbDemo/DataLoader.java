package com.example.imdbDemo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    public Database database;

    public DataLoader(Database database) {
        this.database = database;
    }

    @Override
    public void run(String... args) throws Exception {
        database.processTheData();
    }
}
