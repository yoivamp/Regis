package com.regisbackend.regisbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author 喵vamp
 */
@ServletComponentScan("com.regisbackend.regisbackend.filter")
@SpringBootApplication
@EnableTransactionManagement
public class RegisBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegisBackendApplication.class, args);
    }

}
