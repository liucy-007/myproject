package com.inspur.db;



import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * @author WX0liucy
 */

@SpringBootApplication
@MapperScan("com.inspur.db.dao")
@EnableTransactionManagement
public class SpringBootApplicationRunner {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootApplicationRunner.class, args);
    }
}

