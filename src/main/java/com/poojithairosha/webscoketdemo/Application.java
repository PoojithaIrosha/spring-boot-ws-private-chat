package com.poojithairosha.webscoketdemo;

import com.poojithairosha.webscoketdemo.user.User;
import com.poojithairosha.webscoketdemo.user.UserRepository;
import com.poojithairosha.webscoketdemo.user.UserRole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            userRepository.saveAll(List.of(

                    User.builder()
                    .name("John Doe")
                    .username("john")
                    .password(passwordEncoder.encode("123"))
                    .role(UserRole.ROLE_USER)
                    .build(),

                    User.builder()
                    .name("Peter Parker")
                    .username("peter")
                    .password(passwordEncoder.encode("123"))
                    .role(UserRole.ROLE_USER)
                    .build())

            );
        };
    }
}
