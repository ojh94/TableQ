package com.itschool;

import com.itschool.tableq.dao.UserDao;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringBootDeveloperApplication {
    public static void main(String[] args) {
        UserDao userDao = new UserDao();
        SpringApplication.run(SpringBootDeveloperApplication.class, args);
    }
}
