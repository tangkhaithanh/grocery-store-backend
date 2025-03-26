package org.api.grocerystorebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"org.api.grocerystorebackend"})
public class GroceryStoreBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(GroceryStoreBackendApplication.class, args);
    }

}
