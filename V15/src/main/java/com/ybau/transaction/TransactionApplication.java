package com.ybau.transaction;

import com.ybau.transaction.util.JsonUtil;
import com.ybau.transaction.util.JwtUtil;
import com.ybau.transaction.util.RestURLUtil;
import com.ybau.transaction.util.SHAUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@MapperScan(value = "com.ybau.transaction.mapper")
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class TransactionApplication {
    public static void main(String[] args) {
        SpringApplication.run(TransactionApplication.class, args);
    }

    @Bean
    public JwtUtil getJwtUtil() {
        return new JwtUtil();
    }

    @Bean
    public SHAUtil getShaUtil() {
        return new SHAUtil();
    }

    @Bean
    public JsonUtil getJsonUtil() {
        return new JsonUtil();
    }

    @Bean
    public RestURLUtil getRestURLUtil() {
        return new RestURLUtil();
    }

}
