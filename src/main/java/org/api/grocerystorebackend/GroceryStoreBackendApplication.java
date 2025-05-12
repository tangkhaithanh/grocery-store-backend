package org.api.grocerystorebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"org.api.grocerystorebackend"})
@EnableScheduling
public class GroceryStoreBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(GroceryStoreBackendApplication.class, args);
    }

}
