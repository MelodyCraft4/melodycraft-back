package com.melody;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@Slf4j
public class MelodyServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MelodyServerApplication.class, args);
        log.info("server started");
    }

}
