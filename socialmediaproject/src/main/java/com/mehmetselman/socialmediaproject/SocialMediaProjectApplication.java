package com.mehmetselman.socialmediaproject;

import com.mehmetselman.socialmediaproject.config.AuthFilter;
import com.mehmetselman.socialmediaproject.entity.User;
import com.mehmetselman.socialmediaproject.enums.Role;
import com.mehmetselman.socialmediaproject.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SocialMediaProjectApplication {

    @Autowired
    private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(SocialMediaProjectApplication.class, args);
	}

    //Baslangicta admin kullanici olusturmak icin
    @PostConstruct
    public void initAdmin(){
        if (userRepository.findByUsername("admin") == null){
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin123");
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
        }
    }
}
